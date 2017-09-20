package com.escobar.bmsapp.web.rest;

import com.escobar.bmsapp.BmsappApp;

import com.escobar.bmsapp.domain.Routes;
import com.escobar.bmsapp.repository.RoutesRepository;
import com.escobar.bmsapp.service.RoutesService;
import com.escobar.bmsapp.repository.search.RoutesSearchRepository;
import com.escobar.bmsapp.service.dto.RoutesDTO;
import com.escobar.bmsapp.service.mapper.RoutesMapper;
import com.escobar.bmsapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoutesResource REST controller.
 *
 * @see RoutesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BmsappApp.class)
public class RoutesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_VALID_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_TO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_VALID_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_FROM = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    private RoutesMapper routesMapper;

    @Autowired
    private RoutesService routesService;

    @Autowired
    private RoutesSearchRepository routesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRoutesMockMvc;

    private Routes routes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoutesResource routesResource = new RoutesResource(routesService);
        this.restRoutesMockMvc = MockMvcBuilders.standaloneSetup(routesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Routes createEntity(EntityManager em) {
        Routes routes = new Routes()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .validTo(DEFAULT_VALID_TO)
            .validFrom(DEFAULT_VALID_FROM);
        return routes;
    }

    @Before
    public void initTest() {
        routesSearchRepository.deleteAll();
        routes = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoutes() throws Exception {
        int databaseSizeBeforeCreate = routesRepository.findAll().size();

        // Create the Routes
        RoutesDTO routesDTO = routesMapper.toDto(routes);
        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routesDTO)))
            .andExpect(status().isCreated());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeCreate + 1);
        Routes testRoutes = routesList.get(routesList.size() - 1);
        assertThat(testRoutes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRoutes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoutes.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
        assertThat(testRoutes.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);

        // Validate the Routes in Elasticsearch
        Routes routesEs = routesSearchRepository.findOne(testRoutes.getId());
        assertThat(routesEs).isEqualToComparingFieldByField(testRoutes);
    }

    @Test
    @Transactional
    public void createRoutesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = routesRepository.findAll().size();

        // Create the Routes with an existing ID
        routes.setId(1L);
        RoutesDTO routesDTO = routesMapper.toDto(routes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = routesRepository.findAll().size();
        // set the field null
        routes.setCode(null);

        // Create the Routes, which fails.
        RoutesDTO routesDTO = routesMapper.toDto(routes);

        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routesDTO)))
            .andExpect(status().isBadRequest());

        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = routesRepository.findAll().size();
        // set the field null
        routes.setDescription(null);

        // Create the Routes, which fails.
        RoutesDTO routesDTO = routesMapper.toDto(routes);

        restRoutesMockMvc.perform(post("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routesDTO)))
            .andExpect(status().isBadRequest());

        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);

        // Get all the routesList
        restRoutesMockMvc.perform(get("/api/routes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(routes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())));
    }

    @Test
    @Transactional
    public void getRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);

        // Get the routes
        restRoutesMockMvc.perform(get("/api/routes/{id}", routes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(routes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoutes() throws Exception {
        // Get the routes
        restRoutesMockMvc.perform(get("/api/routes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        routesSearchRepository.save(routes);
        int databaseSizeBeforeUpdate = routesRepository.findAll().size();

        // Update the routes
        Routes updatedRoutes = routesRepository.findOne(routes.getId());
        updatedRoutes
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .validTo(UPDATED_VALID_TO)
            .validFrom(UPDATED_VALID_FROM);
        RoutesDTO routesDTO = routesMapper.toDto(updatedRoutes);

        restRoutesMockMvc.perform(put("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routesDTO)))
            .andExpect(status().isOk());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeUpdate);
        Routes testRoutes = routesList.get(routesList.size() - 1);
        assertThat(testRoutes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRoutes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoutes.getValidTo()).isEqualTo(UPDATED_VALID_TO);
        assertThat(testRoutes.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);

        // Validate the Routes in Elasticsearch
        Routes routesEs = routesSearchRepository.findOne(testRoutes.getId());
        assertThat(routesEs).isEqualToComparingFieldByField(testRoutes);
    }

    @Test
    @Transactional
    public void updateNonExistingRoutes() throws Exception {
        int databaseSizeBeforeUpdate = routesRepository.findAll().size();

        // Create the Routes
        RoutesDTO routesDTO = routesMapper.toDto(routes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRoutesMockMvc.perform(put("/api/routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(routesDTO)))
            .andExpect(status().isCreated());

        // Validate the Routes in the database
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        routesSearchRepository.save(routes);
        int databaseSizeBeforeDelete = routesRepository.findAll().size();

        // Get the routes
        restRoutesMockMvc.perform(delete("/api/routes/{id}", routes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean routesExistsInEs = routesSearchRepository.exists(routes.getId());
        assertThat(routesExistsInEs).isFalse();

        // Validate the database is empty
        List<Routes> routesList = routesRepository.findAll();
        assertThat(routesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        routesSearchRepository.save(routes);

        // Search the routes
        restRoutesMockMvc.perform(get("/api/_search/routes?query=id:" + routes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(routes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Routes.class);
        Routes routes1 = new Routes();
        routes1.setId(1L);
        Routes routes2 = new Routes();
        routes2.setId(routes1.getId());
        assertThat(routes1).isEqualTo(routes2);
        routes2.setId(2L);
        assertThat(routes1).isNotEqualTo(routes2);
        routes1.setId(null);
        assertThat(routes1).isNotEqualTo(routes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoutesDTO.class);
        RoutesDTO routesDTO1 = new RoutesDTO();
        routesDTO1.setId(1L);
        RoutesDTO routesDTO2 = new RoutesDTO();
        assertThat(routesDTO1).isNotEqualTo(routesDTO2);
        routesDTO2.setId(routesDTO1.getId());
        assertThat(routesDTO1).isEqualTo(routesDTO2);
        routesDTO2.setId(2L);
        assertThat(routesDTO1).isNotEqualTo(routesDTO2);
        routesDTO1.setId(null);
        assertThat(routesDTO1).isNotEqualTo(routesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(routesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(routesMapper.fromId(null)).isNull();
    }
}
