package com.astontech.boozhound.service.impl;

import com.astontech.boozhound.service.DistillerService;
import com.astontech.boozhound.domain.Distiller;
import com.astontech.boozhound.repository.DistillerRepository;
import com.astontech.boozhound.repository.search.DistillerSearchRepository;
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
 * Service Implementation for managing Distiller.
 */
@Service
@Transactional
public class DistillerServiceImpl implements DistillerService{

    private final Logger log = LoggerFactory.getLogger(DistillerServiceImpl.class);
    
    @Inject
    private DistillerRepository distillerRepository;

    @Inject
    private DistillerSearchRepository distillerSearchRepository;

    /**
     * Save a distiller.
     *
     * @param distiller the entity to save
     * @return the persisted entity
     */
    public Distiller save(Distiller distiller) {
        log.debug("Request to save Distiller : {}", distiller);
        Distiller result = distillerRepository.save(distiller);
        distillerSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the distillers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Distiller> findAll(Pageable pageable) {
        log.debug("Request to get all Distillers");
        Page<Distiller> result = distillerRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the distillers where Checkin is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Distiller> findAllWhereCheckinIsNull() {
        log.debug("Request to get all distillers where Checkin is null");
        return StreamSupport
            .stream(distillerRepository.findAll().spliterator(), false)
            .filter(distiller -> distiller.getCheckin() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one distiller by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Distiller findOne(Long id) {
        log.debug("Request to get Distiller : {}", id);
        Distiller distiller = distillerRepository.findOne(id);
        return distiller;
    }

    /**
     *  Delete the  distiller by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Distiller : {}", id);
        distillerRepository.delete(id);
        distillerSearchRepository.delete(id);
    }

    /**
     * Search for the distiller corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Distiller> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Distillers for query {}", query);
        Page<Distiller> result = distillerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
