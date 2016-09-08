package com.astontech.boozhound.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.astontech.boozhound.domain.Bourbon;
import com.astontech.boozhound.service.BourbonService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bourbon.
 */
@RestController
@RequestMapping("/api")
public class BourbonResource {

    private final Logger log = LoggerFactory.getLogger(BourbonResource.class);
        
    @Inject
    private BourbonService bourbonService;

    /**
     * POST  /bourbons : Create a new bourbon.
     *
     * @param bourbon the bourbon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bourbon, or with status 400 (Bad Request) if the bourbon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bourbons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bourbon> createBourbon(@Valid @RequestBody Bourbon bourbon) throws URISyntaxException {
        log.debug("REST request to save Bourbon : {}", bourbon);
        if (bourbon.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bourbon", "idexists", "A new bourbon cannot already have an ID")).body(null);
        }
        Bourbon result = bourbonService.save(bourbon);
        return ResponseEntity.created(new URI("/api/bourbons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bourbon", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bourbons : Updates an existing bourbon.
     *
     * @param bourbon the bourbon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bourbon,
     * or with status 400 (Bad Request) if the bourbon is not valid,
     * or with status 500 (Internal Server Error) if the bourbon couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bourbons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bourbon> updateBourbon(@Valid @RequestBody Bourbon bourbon) throws URISyntaxException {
        log.debug("REST request to update Bourbon : {}", bourbon);
        if (bourbon.getId() == null) {
            return createBourbon(bourbon);
        }
        Bourbon result = bourbonService.save(bourbon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bourbon", bourbon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bourbons : get all the bourbons.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of bourbons in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bourbons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bourbon>> getAllBourbons(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("checkin-is-null".equals(filter)) {
            log.debug("REST request to get all Bourbons where checkin is null");
            return new ResponseEntity<>(bourbonService.findAllWhereCheckinIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Bourbons");
        Page<Bourbon> page = bourbonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bourbons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bourbons/:id : get the "id" bourbon.
     *
     * @param id the id of the bourbon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bourbon, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bourbons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bourbon> getBourbon(@PathVariable Long id) {
        log.debug("REST request to get Bourbon : {}", id);
        Bourbon bourbon = bourbonService.findOne(id);
        return Optional.ofNullable(bourbon)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bourbons/:id : delete the "id" bourbon.
     *
     * @param id the id of the bourbon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bourbons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBourbon(@PathVariable Long id) {
        log.debug("REST request to delete Bourbon : {}", id);
        bourbonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bourbon", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bourbons?query=:query : search for the bourbon corresponding
     * to the query.
     *
     * @param query the query of the bourbon search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/bourbons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bourbon>> searchBourbons(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Bourbons for query {}", query);
        Page<Bourbon> page = bourbonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bourbons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
