package com.astontech.boozhound.service.impl;

import com.astontech.boozhound.service.BourbonService;
import com.astontech.boozhound.domain.Bourbon;
import com.astontech.boozhound.repository.BourbonRepository;
import com.astontech.boozhound.repository.search.BourbonSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Bourbon.
 */
@Service
@Transactional
public class BourbonServiceImpl implements BourbonService{

    private final Logger log = LoggerFactory.getLogger(BourbonServiceImpl.class);
    
    @Inject
    private BourbonRepository bourbonRepository;

    @Inject
    private BourbonSearchRepository bourbonSearchRepository;

    /**
     * Save a bourbon.
     *
     * @param bourbon the entity to save
     * @return the persisted entity
     */
    public Bourbon save(Bourbon bourbon) {
        log.debug("Request to save Bourbon : {}", bourbon);
        Bourbon result = bourbonRepository.save(bourbon);
        bourbonSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the bourbons.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Bourbon> findAll(Pageable pageable) {
        log.debug("Request to get all Bourbons");
        Page<Bourbon> result = bourbonRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the bourbons where Checkin is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Bourbon> findAllWhereCheckinIsNull() {
        log.debug("Request to get all bourbons where Checkin is null");
        return StreamSupport
            .stream(bourbonRepository.findAll().spliterator(), false)
            .filter(bourbon -> bourbon.getCheckin() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one bourbon by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Bourbon findOne(Long id) {
        log.debug("Request to get Bourbon : {}", id);
        Bourbon bourbon = bourbonRepository.findOne(id);
        return bourbon;
    }

    /**
     *  Delete the  bourbon by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bourbon : {}", id);
        bourbonRepository.delete(id);
        bourbonSearchRepository.delete(id);
    }

    /**
     * Search for the bourbon corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Bourbon> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bourbons for query {}", query);
        Page<Bourbon> result = bourbonSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
