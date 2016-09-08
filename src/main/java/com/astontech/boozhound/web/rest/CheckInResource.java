package com.astontech.boozhound.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.astontech.boozhound.domain.CheckIn;
import com.astontech.boozhound.service.CheckInService;
import com.astontech.boozhound.web.rest.util.HeaderUtil;
import com.astontech.boozhound.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CheckIn.
 */
@RestController
@RequestMapping("/api")
public class CheckInResource {

    private final Logger log = LoggerFactory.getLogger(CheckInResource.class);
        
    @Inject
    private CheckInService checkInService;

    /**
     * POST  /check-ins : Create a new checkIn.
     *
     * @param checkIn the checkIn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checkIn, or with status 400 (Bad Request) if the checkIn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/check-ins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CheckIn> createCheckIn(@RequestBody CheckIn checkIn) throws URISyntaxException {
        log.debug("REST request to save CheckIn : {}", checkIn);
        if (checkIn.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("checkIn", "idexists", "A new checkIn cannot already have an ID")).body(null);
        }
        CheckIn result = checkInService.save(checkIn);
        return ResponseEntity.created(new URI("/api/check-ins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("checkIn", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /check-ins : Updates an existing checkIn.
     *
     * @param checkIn the checkIn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checkIn,
     * or with status 400 (Bad Request) if the checkIn is not valid,
     * or with status 500 (Internal Server Error) if the checkIn couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/check-ins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CheckIn> updateCheckIn(@RequestBody CheckIn checkIn) throws URISyntaxException {
        log.debug("REST request to update CheckIn : {}", checkIn);
        if (checkIn.getId() == null) {
            return createCheckIn(checkIn);
        }
        CheckIn result = checkInService.save(checkIn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("checkIn", checkIn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /check-ins : get all the checkIns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checkIns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/check-ins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CheckIn>> getAllCheckIns(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CheckIns");
        Page<CheckIn> page = checkInService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/check-ins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /check-ins/:id : get the "id" checkIn.
     *
     * @param id the id of the checkIn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checkIn, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/check-ins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CheckIn> getCheckIn(@PathVariable Long id) {
        log.debug("REST request to get CheckIn : {}", id);
        CheckIn checkIn = checkInService.findOne(id);
        return Optional.ofNullable(checkIn)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /check-ins/:id : delete the "id" checkIn.
     *
     * @param id the id of the checkIn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/check-ins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCheckIn(@PathVariable Long id) {
        log.debug("REST request to delete CheckIn : {}", id);
        checkInService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("checkIn", id.toString())).build();
    }

    /**
     * SEARCH  /_search/check-ins?query=:query : search for the checkIn corresponding
     * to the query.
     *
     * @param query the query of the checkIn search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/check-ins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CheckIn>> searchCheckIns(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CheckIns for query {}", query);
        Page<CheckIn> page = checkInService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/check-ins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
