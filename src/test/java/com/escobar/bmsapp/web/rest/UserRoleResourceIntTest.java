package com.escobar.bmsapp.web.rest;

import com.escobar.bmsapp.BmsappApp;

import com.escobar.bmsapp.domain.UserRole;
import com.escobar.bmsapp.repository.UserRoleRepository;
import com.escobar.bmsapp.service.UserRoleService;
import com.escobar.bmsapp.repository.search.UserRoleSearchRepository;
import com.escobar.bmsapp.service.dto.UserRoleDTO;
import com.escobar.bmsapp.service.mapper.UserRoleMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserRoleResource REST controller.
 *
 * @see UserRoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BmsappApp.class)
public class UserRoleResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleSearchRepository userRoleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserRoleMockMvc;

    private UserRole userRole;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserRoleResource userRoleResource = new UserRoleResource(userRoleService);
        this.restUserRoleMockMvc = MockMvcBuilders.standaloneSetup(userRoleResource)
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
    public static UserRole createEntity(EntityManager em) {
        UserRole userRole = new UserRole()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return userRole;
    }

    @Before
    public void initTest() {
        userRoleSearchRepository.deleteAll();
        userRole = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserRole() throws Exception {
        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);
        restUserRoleMockMvc.perform(post("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeCreate + 1);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUserRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the UserRole in Elasticsearch
        UserRole userRoleEs = userRoleSearchRepository.findOne(testUserRole.getId());
        assertThat(userRoleEs).isEqualToComparingFieldByField(testUserRole);
    }

    @Test
    @Transactional
    public void createUserRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();

        // Create the UserRole with an existing ID
        userRole.setId(1L);
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRoleMockMvc.perform(post("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRoleRepository.findAll().size();
        // set the field null
        userRole.setCode(null);

        // Create the UserRole, which fails.
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        restUserRoleMockMvc.perform(post("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleDTO)))
            .andExpect(status().isBadRequest());

        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserRoles() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList
        restUserRoleMockMvc.perform(get("/api/user-roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get the userRole
        restUserRoleMockMvc.perform(get("/api/user-roles/{id}", userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userRole.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserRole() throws Exception {
        // Get the userRole
        restUserRoleMockMvc.perform(get("/api/user-roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);
        userRoleSearchRepository.save(userRole);
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Update the userRole
        UserRole updatedUserRole = userRoleRepository.findOne(userRole.getId());
        updatedUserRole
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(updatedUserRole);

        restUserRoleMockMvc.perform(put("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleDTO)))
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the UserRole in Elasticsearch
        UserRole userRoleEs = userRoleSearchRepository.findOne(testUserRole.getId());
        assertThat(userRoleEs).isEqualToComparingFieldByField(testUserRole);
    }

    @Test
    @Transactional
    public void updateNonExistingUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Create the UserRole
        UserRoleDTO userRoleDTO = userRoleMapper.toDto(userRole);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserRoleMockMvc.perform(put("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);
        userRoleSearchRepository.save(userRole);
        int databaseSizeBeforeDelete = userRoleRepository.findAll().size();

        // Get the userRole
        restUserRoleMockMvc.perform(delete("/api/user-roles/{id}", userRole.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userRoleExistsInEs = userRoleSearchRepository.exists(userRole.getId());
        assertThat(userRoleExistsInEs).isFalse();

        // Validate the database is empty
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);
        userRoleSearchRepository.save(userRole);

        // Search the userRole
        restUserRoleMockMvc.perform(get("/api/_search/user-roles?query=id:" + userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRole.class);
        UserRole userRole1 = new UserRole();
        userRole1.setId(1L);
        UserRole userRole2 = new UserRole();
        userRole2.setId(userRole1.getId());
        assertThat(userRole1).isEqualTo(userRole2);
        userRole2.setId(2L);
        assertThat(userRole1).isNotEqualTo(userRole2);
        userRole1.setId(null);
        assertThat(userRole1).isNotEqualTo(userRole2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRoleDTO.class);
        UserRoleDTO userRoleDTO1 = new UserRoleDTO();
        userRoleDTO1.setId(1L);
        UserRoleDTO userRoleDTO2 = new UserRoleDTO();
        assertThat(userRoleDTO1).isNotEqualTo(userRoleDTO2);
        userRoleDTO2.setId(userRoleDTO1.getId());
        assertThat(userRoleDTO1).isEqualTo(userRoleDTO2);
        userRoleDTO2.setId(2L);
        assertThat(userRoleDTO1).isNotEqualTo(userRoleDTO2);
        userRoleDTO1.setId(null);
        assertThat(userRoleDTO1).isNotEqualTo(userRoleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userRoleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userRoleMapper.fromId(null)).isNull();
    }
}
