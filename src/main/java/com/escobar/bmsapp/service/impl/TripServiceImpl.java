package com.escobar.bmsapp.service.impl;

import com.escobar.bmsapp.service.TripService;
import com.escobar.bmsapp.domain.Trip;
import com.escobar.bmsapp.repository.TripRepository;
import com.escobar.bmsapp.repository.search.TripSearchRepository;
import com.escobar.bmsapp.service.dto.TripDTO;
import com.escobar.bmsapp.service.mapper.TripMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Trip.
 */
@Service
@Transactional
public class TripServiceImpl implements TripService{

    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;

    private final TripSearchRepository tripSearchRepository;

    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper, TripSearchRepository tripSearchRepository) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.tripSearchRepository = tripSearchRepository;
    }

    /**
     * Save a trip.
     *
     * @param tripDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TripDTO save(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        TripDTO result = tripMapper.toDto(trip);
        tripSearchRepository.save(trip);
        return result;
    }

    /**
     *  Get all the trips.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Trips");
        return tripRepository.findAll(pageable)
            .map(tripMapper::toDto);
    }

    /**
     *  Get one trip by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TripDTO findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        Trip trip = tripRepository.findOne(id);
        return tripMapper.toDto(trip);
    }

    /**
     *  Delete the  trip by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        tripRepository.delete(id);
        tripSearchRepository.delete(id);
    }

    /**
     * Search for the trip corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Trips for query {}", query);
        Page<Trip> result = tripSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(tripMapper::toDto);
    }
}
