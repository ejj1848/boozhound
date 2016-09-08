package com.astontech.boozhound.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Location entity.
 */
public class LocationDTO implements Serializable {

    private Long id;

    @NotNull
    private String locationName;

    private String streetAddress;

    private String postalCode;

    private String city;

    private String state;

    private Integer geoLat;

    private Integer geoLng;


    private Long distillerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public Integer getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(Integer geoLat) {
        this.geoLat = geoLat;
    }
    public Integer getGeoLng() {
        return geoLng;
    }

    public void setGeoLng(Integer geoLng) {
        this.geoLng = geoLng;
    }

    public Long getDistillerId() {
        return distillerId;
    }

    public void setDistillerId(Long distillerId) {
        this.distillerId = distillerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;

        if ( ! Objects.equals(id, locationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + id +
            ", locationName='" + locationName + "'" +
            ", streetAddress='" + streetAddress + "'" +
            ", postalCode='" + postalCode + "'" +
            ", city='" + city + "'" +
            ", state='" + state + "'" +
            ", geoLat='" + geoLat + "'" +
            ", geoLng='" + geoLng + "'" +
            '}';
    }
}
