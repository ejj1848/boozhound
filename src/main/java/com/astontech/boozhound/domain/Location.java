package com.astontech.boozhound.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "geo_lat")
    private Integer geoLat;

    @Column(name = "geo_lng")
    private Integer geoLng;

    @ManyToOne
    private Distiller distiller;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private CheckIn checkin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public Location locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Location streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Location postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public Location state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getGeoLat() {
        return geoLat;
    }

    public Location geoLat(Integer geoLat) {
        this.geoLat = geoLat;
        return this;
    }

    public void setGeoLat(Integer geoLat) {
        this.geoLat = geoLat;
    }

    public Integer getGeoLng() {
        return geoLng;
    }

    public Location geoLng(Integer geoLng) {
        this.geoLng = geoLng;
        return this;
    }

    public void setGeoLng(Integer geoLng) {
        this.geoLng = geoLng;
    }

    public Distiller getDistiller() {
        return distiller;
    }

    public Location distiller(Distiller distiller) {
        this.distiller = distiller;
        return this;
    }

    public void setDistiller(Distiller distiller) {
        this.distiller = distiller;
    }

    public CheckIn getCheckin() {
        return checkin;
    }

    public Location checkin(CheckIn checkIn) {
        this.checkin = checkIn;
        return this;
    }

    public void setCheckin(CheckIn checkIn) {
        this.checkin = checkIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        if(location.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Location{" +
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
