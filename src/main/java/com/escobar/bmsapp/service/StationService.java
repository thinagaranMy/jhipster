package com.escobar.bmsapp.service;

import com.escobar.bmsapp.service.dto.StationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Station.
 */
public interface StationService {

    /**
     * Save a station.
     *
     * @param stationDTO the entity to save
     * @return the persisted entity
     */
    StationDTO save(StationDTO stationDTO);

    /**
     *  Get all the stations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StationDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" station.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StationDTO findOne(Long id);

    /**
     *  Delete the "id" station.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the station corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StationDTO> search(String query, Pageable pageable);
}
