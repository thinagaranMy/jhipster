package com.escobar.bmsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.escobar.bmsapp.service.TripService;
import com.escobar.bmsapp.web.rest.util.HeaderUtil;
import com.escobar.bmsapp.web.rest.util.PaginationUtil;
import com.escobar.bmsapp.service.dto.TripDTO;
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
 * REST controller for managing Trip.
 */
@RestController
@RequestMapping("/api")
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    private final TripService tripService;

    public TripResource(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * POST  /trips : Create a new trip.
     *
     * @param tripDTO the tripDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tripDTO, or with status 400 (Bad Request) if the trip has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trips")
    @Timed
    public ResponseEntity<TripDTO> createTrip(@Valid @RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", tripDTO);
        if (tripDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trip cannot already have an ID")).body(null);
        }
        TripDTO result = tripService.save(tripDTO);
        return ResponseEntity.created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trips : Updates an existing trip.
     *
     * @param tripDTO the tripDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tripDTO,
     * or with status 400 (Bad Request) if the tripDTO is not valid,
     * or with status 500 (Internal Server Error) if the tripDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trips")
    @Timed
    public ResponseEntity<TripDTO> updateTrip(@Valid @RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to update Trip : {}", tripDTO);
        if (tripDTO.getId() == null) {
            return createTrip(tripDTO);
        }
        TripDTO result = tripService.save(tripDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tripDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trips : get all the trips.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trips in body
     */
    @GetMapping("/trips")
    @Timed
    public ResponseEntity<List<TripDTO>> getAllTrips(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Trips");
        Page<TripDTO> page = tripService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trips");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trips/:id : get the "id" trip.
     *
     * @param id the id of the tripDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tripDTO, or with status 404 (Not Found)
     */
    @GetMapping("/trips/{id}")
    @Timed
    public ResponseEntity<TripDTO> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        TripDTO tripDTO = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tripDTO));
    }

    /**
     * DELETE  /trips/:id : delete the "id" trip.
     *
     * @param id the id of the tripDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trips/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/trips?query=:query : search for the trip corresponding
     * to the query.
     *
     * @param query the query of the trip search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/trips")
    @Timed
    public ResponseEntity<List<TripDTO>> searchTrips(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Trips for query {}", query);
        Page<TripDTO> page = tripService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/trips");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
