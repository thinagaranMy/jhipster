package com.escobar.bmsapp.service.impl;

import com.escobar.bmsapp.service.RoutesService;
import com.escobar.bmsapp.domain.Routes;
import com.escobar.bmsapp.repository.RoutesRepository;
import com.escobar.bmsapp.repository.search.RoutesSearchRepository;
import com.escobar.bmsapp.service.dto.RoutesDTO;
import com.escobar.bmsapp.service.mapper.RoutesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Routes.
 */
@Service
@Transactional
public class RoutesServiceImpl implements RoutesService{

    private final Logger log = LoggerFactory.getLogger(RoutesServiceImpl.class);

    private final RoutesRepository routesRepository;

    private final RoutesMapper routesMapper;

    private final RoutesSearchRepository routesSearchRepository;

    public RoutesServiceImpl(RoutesRepository routesRepository, RoutesMapper routesMapper, RoutesSearchRepository routesSearchRepository) {
        this.routesRepository = routesRepository;
        this.routesMapper = routesMapper;
        this.routesSearchRepository = routesSearchRepository;
    }

    /**
     * Save a routes.
     *
     * @param routesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RoutesDTO save(RoutesDTO routesDTO) {
        log.debug("Request to save Routes : {}", routesDTO);
        Routes routes = routesMapper.toEntity(routesDTO);
        routes = routesRepository.save(routes);
        RoutesDTO result = routesMapper.toDto(routes);
        routesSearchRepository.save(routes);
        return result;
    }

    /**
     *  Get all the routes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RoutesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Routes");
        return routesRepository.findAll(pageable)
            .map(routesMapper::toDto);
    }

    /**
     *  Get one routes by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RoutesDTO findOne(Long id) {
        log.debug("Request to get Routes : {}", id);
        Routes routes = routesRepository.findOneWithEagerRelationships(id);
        return routesMapper.toDto(routes);
    }

    /**
     *  Delete the  routes by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Routes : {}", id);
        routesRepository.delete(id);
        routesSearchRepository.delete(id);
    }

    /**
     * Search for the routes corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RoutesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Routes for query {}", query);
        Page<Routes> result = routesSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(routesMapper::toDto);
    }
}
