package com.astontech.boozhound.service;

import com.astontech.boozhound.domain.Location;

import java.util.List;

/**
 * Service Interface for managing Location.
 */
public interface LocationService {

    /**
     * Save a location.
     *
     * @param location the entity to save
     * @return the persisted entity
     */
    Location save(Location location);

    /**
     *  Get all the locations.
     *  
     *  @return the list of entities
     */
    List<Location> findAll();
    /**
     *  Get all the LocationDTO where Checkin is null.
     *
     *  @return the list of entities
     */
    List<Location> findAllWhereCheckinIsNull();

    /**
     *  Get the "id" location.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Location findOne(Long id);

    /**
     *  Delete the "id" location.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the location corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<Location> search(String query);
}
