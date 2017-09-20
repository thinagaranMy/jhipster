package com.escobar.bmsapp.service.impl;

import com.escobar.bmsapp.service.StationService;
import com.escobar.bmsapp.domain.Station;
import com.escobar.bmsapp.repository.StationRepository;
import com.escobar.bmsapp.repository.search.StationSearchRepository;
import com.escobar.bmsapp.service.dto.StationDTO;
import com.escobar.bmsapp.service.mapper.StationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Station.
 */
@Service
@Transactional
public class StationServiceImpl implements StationService{

    private final Logger log = LoggerFactory.getLogger(StationServiceImpl.class);

    private final StationRepository stationRepository;

    private final StationMapper stationMapper;

    private final StationSearchRepository stationSearchRepository;

    public StationServiceImpl(StationRepository stationRepository, StationMapper stationMapper, StationSearchRepository stationSearchRepository) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
        this.stationSearchRepository = stationSearchRepository;
    }

    /**
     * Save a station.
     *
     * @param stationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StationDTO save(StationDTO stationDTO) {
        log.debug("Request to save Station : {}", stationDTO);
        Station station = stationMapper.toEntity(stationDTO);
        station = stationRepository.save(station);
        StationDTO result = stationMapper.toDto(station);
        stationSearchRepository.save(station);
        return result;
    }

    /**
     *  Get all the stations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stations");
        return stationRepository.findAll(pageable)
            .map(stationMapper::toDto);
    }

    /**
     *  Get one station by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StationDTO findOne(Long id) {
        log.debug("Request to get Station : {}", id);
        Station station = stationRepository.findOne(id);
        return stationMapper.toDto(station);
    }

    /**
     *  Delete the  station by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Station : {}", id);
        stationRepository.delete(id);
        stationSearchRepository.delete(id);
    }

    /**
     * Search for the station corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stations for query {}", query);
        Page<Station> result = stationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(stationMapper::toDto);
    }
}
