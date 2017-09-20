package com.escobar.bmsapp.service;

import com.escobar.bmsapp.service.dto.UserRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing UserRole.
 */
public interface UserRoleService {

    /**
     * Save a userRole.
     *
     * @param userRoleDTO the entity to save
     * @return the persisted entity
     */
    UserRoleDTO save(UserRoleDTO userRoleDTO);

    /**
     *  Get all the userRoles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserRoleDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userRole.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserRoleDTO findOne(Long id);

    /**
     *  Delete the "id" userRole.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userRole corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserRoleDTO> search(String query, Pageable pageable);
}
