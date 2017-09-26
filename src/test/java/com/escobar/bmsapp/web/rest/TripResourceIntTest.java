package com.escobar.bmsapp.web.rest;

import com.escobar.bmsapp.BmsappApp;

import com.escobar.bmsapp.domain.Trip;
import com.escobar.bmsapp.domain.Routes;
import com.escobar.bmsapp.repository.TripRepository;
import com.escobar.bmsapp.service.TripService;
import com.escobar.bmsapp.repository.search.TripSearchRepository;
import com.escobar.bmsapp.service.dto.TripDTO;
import com.escobar.bmsapp.service.mapper.TripMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TripResource REST controller.
 *
 * @see TripResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BmsappApp.class)
public class TripResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PASSENGER_COUNT = 0;
    private static final Integer UPDATED_PASSENGER_COUNT = 1;

    private static final Instant DEFAULT_SCHEDULED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DEPARTURE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEPARTURE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARRIVAL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARRIVAL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE_TRIP = false;
    private static final Boolean UPDATED_ACTIVE_TRIP = true;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private TripService tripService;

    @Autowired
    private TripSearchRepository tripSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTripMockMvc;

    private Trip trip;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TripResource tripResource = new TripResource(tripService);
        this.restTripMockMvc = MockMvcBuilders.standaloneSetup(tripResource)
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
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .passengerCount(DEFAULT_PASSENGER_COUNT)
            .scheduledTime(DEFAULT_SCHEDULED_TIME)
            .departureTime(DEFAULT_DEPARTURE_TIME)
            .arrivalTime(DEFAULT_ARRIVAL_TIME)
            .activeTrip(DEFAULT_ACTIVE_TRIP);
        // Add required entity
        Routes routes = RoutesResourceIntTest.createEntity(em);
        em.persist(routes);
        em.flush();
        trip.setRoutes(routes);
        return trip;
    }

    @Before
    public void initTest() {
        tripSearchRepository.deleteAll();
        trip = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrip() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);
        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate + 1);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTrip.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrip.getPassengerCount()).isEqualTo(DEFAULT_PASSENGER_COUNT);
        assertThat(testTrip.getScheduledTime()).isEqualTo(DEFAULT_SCHEDULED_TIME);
        assertThat(testTrip.getDepartureTime()).isEqualTo(DEFAULT_DEPARTURE_TIME);
        assertThat(testTrip.getArrivalTime()).isEqualTo(DEFAULT_ARRIVAL_TIME);
        assertThat(testTrip.isActiveTrip()).isEqualTo(DEFAULT_ACTIVE_TRIP);

        // Validate the Trip in Elasticsearch
        Trip tripEs = tripSearchRepository.findOne(testTrip.getId());
        assertThat(tripEs).isEqualToComparingFieldByField(testTrip);
    }

    @Test
    @Transactional
    public void createTripWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // Create the Trip with an existing ID
        trip.setId(1L);
        TripDTO tripDTO = tripMapper.toDto(trip);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setCode(null);

        // Create the Trip, which fails.
        TripDTO tripDTO = tripMapper.toDto(trip);

        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setDescription(null);

        // Create the Trip, which fails.
        TripDTO tripDTO = tripMapper.toDto(trip);

        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPassengerCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setPassengerCount(null);

        // Create the Trip, which fails.
        TripDTO tripDTO = tripMapper.toDto(trip);

        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkScheduledTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setScheduledTime(null);

        // Create the Trip, which fails.
        TripDTO tripDTO = tripMapper.toDto(trip);

        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepartureTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setDepartureTime(null);

        // Create the Trip, which fails.
        TripDTO tripDTO = tripMapper.toDto(trip);

        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveTripIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setActiveTrip(null);

        // Create the Trip, which fails.
        TripDTO tripDTO = tripMapper.toDto(trip);

        restTripMockMvc.perform(post("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc.perform(get("/api/trips?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].passengerCount").value(hasItem(DEFAULT_PASSENGER_COUNT)))
            .andExpect(jsonPath("$.[*].scheduledTime").value(hasItem(DEFAULT_SCHEDULED_TIME.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(DEFAULT_DEPARTURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(DEFAULT_ARRIVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].activeTrip").value(hasItem(DEFAULT_ACTIVE_TRIP.booleanValue())));
    }

    @Test
    @Transactional
    public void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc.perform(get("/api/trips/{id}", trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.passengerCount").value(DEFAULT_PASSENGER_COUNT))
            .andExpect(jsonPath("$.scheduledTime").value(DEFAULT_SCHEDULED_TIME.toString()))
            .andExpect(jsonPath("$.departureTime").value(DEFAULT_DEPARTURE_TIME.toString()))
            .andExpect(jsonPath("$.arrivalTime").value(DEFAULT_ARRIVAL_TIME.toString()))
            .andExpect(jsonPath("$.activeTrip").value(DEFAULT_ACTIVE_TRIP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get("/api/trips/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        tripSearchRepository.save(trip);
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip
        Trip updatedTrip = tripRepository.findOne(trip.getId());
        updatedTrip
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .passengerCount(UPDATED_PASSENGER_COUNT)
            .scheduledTime(UPDATED_SCHEDULED_TIME)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .activeTrip(UPDATED_ACTIVE_TRIP);
        TripDTO tripDTO = tripMapper.toDto(updatedTrip);

        restTripMockMvc.perform(put("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTrip.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrip.getPassengerCount()).isEqualTo(UPDATED_PASSENGER_COUNT);
        assertThat(testTrip.getScheduledTime()).isEqualTo(UPDATED_SCHEDULED_TIME);
        assertThat(testTrip.getDepartureTime()).isEqualTo(UPDATED_DEPARTURE_TIME);
        assertThat(testTrip.getArrivalTime()).isEqualTo(UPDATED_ARRIVAL_TIME);
        assertThat(testTrip.isActiveTrip()).isEqualTo(UPDATED_ACTIVE_TRIP);

        // Validate the Trip in Elasticsearch
        Trip tripEs = tripSearchRepository.findOne(testTrip.getId());
        assertThat(tripEs).isEqualToComparingFieldByField(testTrip);
    }

    @Test
    @Transactional
    public void updateNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTripMockMvc.perform(put("/api/trips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        tripSearchRepository.save(trip);
        int databaseSizeBeforeDelete = tripRepository.findAll().size();

        // Get the trip
        restTripMockMvc.perform(delete("/api/trips/{id}", trip.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tripExistsInEs = tripSearchRepository.exists(trip.getId());
        assertThat(tripExistsInEs).isFalse();

        // Validate the database is empty
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        tripSearchRepository.save(trip);

        // Search the trip
        restTripMockMvc.perform(get("/api/_search/trips?query=id:" + trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].passengerCount").value(hasItem(DEFAULT_PASSENGER_COUNT)))
            .andExpect(jsonPath("$.[*].scheduledTime").value(hasItem(DEFAULT_SCHEDULED_TIME.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(DEFAULT_DEPARTURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(DEFAULT_ARRIVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].activeTrip").value(hasItem(DEFAULT_ACTIVE_TRIP.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trip.class);
        Trip trip1 = new Trip();
        trip1.setId(1L);
        Trip trip2 = new Trip();
        trip2.setId(trip1.getId());
        assertThat(trip1).isEqualTo(trip2);
        trip2.setId(2L);
        assertThat(trip1).isNotEqualTo(trip2);
        trip1.setId(null);
        assertThat(trip1).isNotEqualTo(trip2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripDTO.class);
        TripDTO tripDTO1 = new TripDTO();
        tripDTO1.setId(1L);
        TripDTO tripDTO2 = new TripDTO();
        assertThat(tripDTO1).isNotEqualTo(tripDTO2);
        tripDTO2.setId(tripDTO1.getId());
        assertThat(tripDTO1).isEqualTo(tripDTO2);
        tripDTO2.setId(2L);
        assertThat(tripDTO1).isNotEqualTo(tripDTO2);
        tripDTO1.setId(null);
        assertThat(tripDTO1).isNotEqualTo(tripDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tripMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tripMapper.fromId(null)).isNull();
    }
}
