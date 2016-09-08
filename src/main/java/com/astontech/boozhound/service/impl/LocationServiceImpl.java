package com.astontech.boozhound.service.impl;

import com.astontech.boozhound.service.LocationService;
import com.astontech.boozhound.domain.Location;
import com.astontech.boozhound.repository.LocationRepository;
import com.astontech.boozhound.repository.search.LocationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService{

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    
    @Inject
    private LocationRepository locationRepository;

    @Inject
    private LocationSearchRepository locationSearchRepository;

    /**
     * Save a location.
     *
     * @param location the entity to save
     * @return the persisted entity
     */
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        Location result = locationRepository.save(location);
        locationSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the locations.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Location> findAll() {
        log.debug("Request to get all Locations");
        List<Location> result = locationRepository.findAll();

        return result;
    }


    /**
     *  get all the locations where Checkin is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Location> findAllWhereCheckinIsNull() {
        log.debug("Request to get all locations where Checkin is null");
        return StreamSupport
            .stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getCheckin() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one location by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Location findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        Location location = locationRepository.findOne(id);
        return location;
    }

    /**
     *  Delete the  location by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.delete(id);
        locationSearchRepository.delete(id);
    }

    /**
     * Search for the location corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Location> search(String query) {
        log.debug("Request to search Locations for query {}", query);
        return StreamSupport
            .stream(locationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
