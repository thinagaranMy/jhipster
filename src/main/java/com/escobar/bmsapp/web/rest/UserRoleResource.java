package com.escobar.bmsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.escobar.bmsapp.service.UserRoleService;
import com.escobar.bmsapp.web.rest.util.HeaderUtil;
import com.escobar.bmsapp.web.rest.util.PaginationUtil;
import com.escobar.bmsapp.service.dto.UserRoleDTO;
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
 * REST controller for managing UserRole.
 */
@RestController
@RequestMapping("/api")
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "userRole";

    private final UserRoleService userRoleService;

    public UserRoleResource(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * POST  /user-roles : Create a new userRole.
     *
     * @param userRoleDTO the userRoleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRoleDTO, or with status 400 (Bad Request) if the userRole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-roles")
    @Timed
    public ResponseEntity<UserRoleDTO> createUserRole(@Valid @RequestBody UserRoleDTO userRoleDTO) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", userRoleDTO);
        if (userRoleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userRole cannot already have an ID")).body(null);
        }
        UserRoleDTO result = userRoleService.save(userRoleDTO);
        return ResponseEntity.created(new URI("/api/user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-roles : Updates an existing userRole.
     *
     * @param userRoleDTO the userRoleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRoleDTO,
     * or with status 400 (Bad Request) if the userRoleDTO is not valid,
     * or with status 500 (Internal Server Error) if the userRoleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-roles")
    @Timed
    public ResponseEntity<UserRoleDTO> updateUserRole(@Valid @RequestBody UserRoleDTO userRoleDTO) throws URISyntaxException {
        log.debug("REST request to update UserRole : {}", userRoleDTO);
        if (userRoleDTO.getId() == null) {
            return createUserRole(userRoleDTO);
        }
        UserRoleDTO result = userRoleService.save(userRoleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-roles : get all the userRoles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userRoles in body
     */
    @GetMapping("/user-roles")
    @Timed
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserRoles");
        Page<UserRoleDTO> page = userRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-roles/:id : get the "id" userRole.
     *
     * @param id the id of the userRoleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRoleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-roles/{id}")
    @Timed
    public ResponseEntity<UserRoleDTO> getUserRole(@PathVariable Long id) {
        log.debug("REST request to get UserRole : {}", id);
        UserRoleDTO userRoleDTO = userRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userRoleDTO));
    }

    /**
     * DELETE  /user-roles/:id : delete the "id" userRole.
     *
     * @param id the id of the userRoleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-roles?query=:query : search for the userRole corresponding
     * to the query.
     *
     * @param query the query of the userRole search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-roles")
    @Timed
    public ResponseEntity<List<UserRoleDTO>> searchUserRoles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of UserRoles for query {}", query);
        Page<UserRoleDTO> page = userRoleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
