package com.astontech.boozhound.service;

import com.astontech.boozhound.domain.Distiller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Distiller.
 */
public interface DistillerService {

    /**
     * Save a distiller.
     *
     * @param distiller the entity to save
     * @return the persisted entity
     */
    Distiller save(Distiller distiller);

    /**
     *  Get all the distillers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Distiller> findAll(Pageable pageable);
    /**
     *  Get all the DistillerDTO where Checkin is null.
     *
     *  @return the list of entities
     */
    List<Distiller> findAllWhereCheckinIsNull();

    /**
     *  Get the "id" distiller.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Distiller findOne(Long id);

    /**
     *  Delete the "id" distiller.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the distiller corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Distiller> search(String query, Pageable pageable);
}
