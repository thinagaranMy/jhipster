package com.escobar.bmsapp.service.impl;

import com.escobar.bmsapp.service.UserRoleService;
import com.escobar.bmsapp.domain.UserRole;
import com.escobar.bmsapp.repository.UserRoleRepository;
import com.escobar.bmsapp.repository.search.UserRoleSearchRepository;
import com.escobar.bmsapp.service.dto.UserRoleDTO;
import com.escobar.bmsapp.service.mapper.UserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserRole.
 */
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService{

    private final Logger log = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    private final UserRoleSearchRepository userRoleSearchRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper, UserRoleSearchRepository userRoleSearchRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
        this.userRoleSearchRepository = userRoleSearchRepository;
    }

    /**
     * Save a userRole.
     *
     * @param userRoleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserRoleDTO save(UserRoleDTO userRoleDTO) {
        log.debug("Request to save UserRole : {}", userRoleDTO);
        UserRole userRole = userRoleMapper.toEntity(userRoleDTO);
        userRole = userRoleRepository.save(userRole);
        UserRoleDTO result = userRoleMapper.toDto(userRole);
        userRoleSearchRepository.save(userRole);
        return result;
    }

    /**
     *  Get all the userRoles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRoles");
        return userRoleRepository.findAll(pageable)
            .map(userRoleMapper::toDto);
    }

    /**
     *  Get one userRole by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserRoleDTO findOne(Long id) {
        log.debug("Request to get UserRole : {}", id);
        UserRole userRole = userRoleRepository.findOne(id);
        return userRoleMapper.toDto(userRole);
    }

    /**
     *  Delete the  userRole by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRole : {}", id);
        userRoleRepository.delete(id);
        userRoleSearchRepository.delete(id);
    }

    /**
     * Search for the userRole corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserRoles for query {}", query);
        Page<UserRole> result = userRoleSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(userRoleMapper::toDto);
    }
}
