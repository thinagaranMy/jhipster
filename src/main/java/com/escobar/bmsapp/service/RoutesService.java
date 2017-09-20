package com.escobar.bmsapp.service;

import com.escobar.bmsapp.service.dto.RoutesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Routes.
 */
public interface RoutesService {

    /**
     * Save a routes.
     *
     * @param routesDTO the entity to save
     * @return the persisted entity
     */
    RoutesDTO save(RoutesDTO routesDTO);

    /**
     *  Get all the routes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RoutesDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" routes.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RoutesDTO findOne(Long id);

    /**
     *  Delete the "id" routes.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the routes corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RoutesDTO> search(String query, Pageable pageable);
}
