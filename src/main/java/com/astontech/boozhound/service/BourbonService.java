package com.astontech.boozhound.service;

import com.astontech.boozhound.domain.Bourbon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Bourbon.
 */
public interface BourbonService {

    /**
     * Save a bourbon.
     *
     * @param bourbon the entity to save
     * @return the persisted entity
     */
    Bourbon save(Bourbon bourbon);

    /**
     *  Get all the bourbons.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Bourbon> findAll(Pageable pageable);
    /**
     *  Get all the BourbonDTO where Checkin is null.
     *
     *  @return the list of entities
     */
    List<Bourbon> findAllWhereCheckinIsNull();

    /**
     *  Get the "id" bourbon.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Bourbon findOne(Long id);

    /**
     *  Delete the "id" bourbon.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bourbon corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Bourbon> search(String query, Pageable pageable);
}
