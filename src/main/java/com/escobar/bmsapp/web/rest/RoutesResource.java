package com.escobar.bmsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.escobar.bmsapp.service.RoutesService;
import com.escobar.bmsapp.web.rest.util.HeaderUtil;
import com.escobar.bmsapp.web.rest.util.PaginationUtil;
import com.escobar.bmsapp.service.dto.RoutesDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Routes.
 */
@RestController
@RequestMapping("/api")
public class RoutesResource {

    private final Logger log = LoggerFactory.getLogger(RoutesResource.class);

    private static final String ENTITY_NAME = "routes";

    private final RoutesService routesService;

    public RoutesResource(RoutesService routesService) {
        this.routesService = routesService;
    }

    /**
     * POST  /routes : Create a new routes.
     *
     * @param routesDTO the routesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new routesDTO, or with status 400 (Bad Request) if the routes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/routes")
    @Timed
    public ResponseEntity<RoutesDTO> createRoutes(@Valid @RequestBody RoutesDTO routesDTO) throws URISyntaxException {
        log.debug("REST request to save Routes : {}", routesDTO);
        if (routesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new routes cannot already have an ID")).body(null);
        }
        RoutesDTO result = routesService.save(routesDTO);
        return ResponseEntity.created(new URI("/api/routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /routes : Updates an existing routes.
     *
     * @param routesDTO the routesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated routesDTO,
     * or with status 400 (Bad Request) if the routesDTO is not valid,
     * or with status 500 (Internal Server Error) if the routesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/routes")
    @Timed
    public ResponseEntity<RoutesDTO> updateRoutes(@Valid @RequestBody RoutesDTO routesDTO) throws URISyntaxException {
        log.debug("REST request to update Routes : {}", routesDTO);
        if (routesDTO.getId() == null) {
            return createRoutes(routesDTO);
        }
        RoutesDTO result = routesService.save(routesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, routesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /routes : get all the routes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of routes in body
     */
    @GetMapping("/routes")
    @Timed
    public ResponseEntity<List<RoutesDTO>> getAllRoutes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Routes");
        Page<RoutesDTO> page = routesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/routes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /routes/:id : get the "id" routes.
     *
     * @param id the id of the routesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the routesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/routes/{id}")
    @Timed
    public ResponseEntity<RoutesDTO> getRoutes(@PathVariable Long id) {
        log.debug("REST request to get Routes : {}", id);
        RoutesDTO routesDTO = routesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(routesDTO));
    }

    /**
     * DELETE  /routes/:id : delete the "id" routes.
     *
     * @param id the id of the routesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/routes/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoutes(@PathVariable Long id) {
        log.debug("REST request to delete Routes : {}", id);
        routesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/routes?query=:query : search for the routes corresponding
     * to the query.
     *
     * @param query the query of the routes search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/routes")
    @Timed
    public ResponseEntity<List<RoutesDTO>> searchRoutes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Routes for query {}", query);
        Page<RoutesDTO> page = routesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/routes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}