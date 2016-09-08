package com.astontech.boozhound.service.impl;

import com.astontech.boozhound.service.CheckInService;
import com.astontech.boozhound.domain.CheckIn;
import com.astontech.boozhound.repository.CheckInRepository;
import com.astontech.boozhound.repository.search.CheckInSearchRepository;
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
 * Service Implementation for managing CheckIn.
 */
@Service
@Transactional
public class CheckInServiceImpl implements CheckInService{

    private final Logger log = LoggerFactory.getLogger(CheckInServiceImpl.class);
    
    @Inject
    private CheckInRepository checkInRepository;

    @Inject
    private CheckInSearchRepository checkInSearchRepository;

    /**
     * Save a checkIn.
     *
     * @param checkIn the entity to save
     * @return the persisted entity
     */
    public CheckIn save(CheckIn checkIn) {
        log.debug("Request to save CheckIn : {}", checkIn);
        CheckIn result = checkInRepository.save(checkIn);
        checkInSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the checkIns.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CheckIn> findAll(Pageable pageable) {
        log.debug("Request to get all CheckIns");
        Page<CheckIn> result = checkInRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one checkIn by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CheckIn findOne(Long id) {
        log.debug("Request to get CheckIn : {}", id);
        CheckIn checkIn = checkInRepository.findOne(id);
        return checkIn;
    }

    /**
     *  Delete the  checkIn by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CheckIn : {}", id);
        checkInRepository.delete(id);
        checkInSearchRepository.delete(id);
    }

    /**
     * Search for the checkIn corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CheckIn> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CheckIns for query {}", query);
        Page<CheckIn> result = checkInSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
