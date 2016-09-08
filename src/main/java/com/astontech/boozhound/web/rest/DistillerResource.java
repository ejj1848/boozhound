package com.astontech.boozhound.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.astontech.boozhound.domain.Distiller;
import com.astontech.boozhound.service.DistillerService;
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
 * REST controller for managing Distiller.
 */
@RestController
@RequestMapping("/api")
public class DistillerResource {

    private final Logger log = LoggerFactory.getLogger(DistillerResource.class);
        
    @Inject
    private DistillerService distillerService;

    /**
     * POST  /distillers : Create a new distiller.
     *
     * @param distiller the distiller to create
     * @return the ResponseEntity with status 201 (Created) and with body the new distiller, or with status 400 (Bad Request) if the distiller has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/distillers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Distiller> createDistiller(@Valid @RequestBody Distiller distiller) throws URISyntaxException {
        log.debug("REST request to save Distiller : {}", distiller);
        if (distiller.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("distiller", "idexists", "A new distiller cannot already have an ID")).body(null);
        }
        Distiller result = distillerService.save(distiller);
        return ResponseEntity.created(new URI("/api/distillers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("distiller", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /distillers : Updates an existing distiller.
     *
     * @param distiller the distiller to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated distiller,
     * or with status 400 (Bad Request) if the distiller is not valid,
     * or with status 500 (Internal Server Error) if the distiller couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/distillers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Distiller> updateDistiller(@Valid @RequestBody Distiller distiller) throws URISyntaxException {
        log.debug("REST request to update Distiller : {}", distiller);
        if (distiller.getId() == null) {
            return createDistiller(distiller);
        }
        Distiller result = distillerService.save(distiller);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("distiller", distiller.getId().toString()))
            .body(result);
    }

    /**
     * GET  /distillers : get all the distillers.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of distillers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/distillers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Distiller>> getAllDistillers(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("checkin-is-null".equals(filter)) {
            log.debug("REST request to get all Distillers where checkin is null");
            return new ResponseEntity<>(distillerService.findAllWhereCheckinIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Distillers");
        Page<Distiller> page = distillerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/distillers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /distillers/:id : get the "id" distiller.
     *
     * @param id the id of the distiller to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the distiller, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/distillers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Distiller> getDistiller(@PathVariable Long id) {
        log.debug("REST request to get Distiller : {}", id);
        Distiller distiller = distillerService.findOne(id);
        return Optional.ofNullable(distiller)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /distillers/:id : delete the "id" distiller.
     *
     * @param id the id of the distiller to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/distillers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDistiller(@PathVariable Long id) {
        log.debug("REST request to delete Distiller : {}", id);
        distillerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("distiller", id.toString())).build();
    }

    /**
     * SEARCH  /_search/distillers?query=:query : search for the distiller corresponding
     * to the query.
     *
     * @param query the query of the distiller search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/distillers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Distiller>> searchDistillers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Distillers for query {}", query);
        Page<Distiller> page = distillerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/distillers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
