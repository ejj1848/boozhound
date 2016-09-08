package com.astontech.boozhound.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the CheckIn entity.
 */
public class CheckInDTO implements Serializable {

    private Long id;

    private String notes;


    private Long locationId;
    
    private Long distillerId;
    
    private Long bourbonId;
    
    private Long personId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getDistillerId() {
        return distillerId;
    }

    public void setDistillerId(Long distillerId) {
        this.distillerId = distillerId;
    }

    public Long getBourbonId() {
        return bourbonId;
    }

    public void setBourbonId(Long bourbonId) {
        this.bourbonId = bourbonId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CheckInDTO checkInDTO = (CheckInDTO) o;

        if ( ! Objects.equals(id, checkInDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CheckInDTO{" +
            "id=" + id +
            ", notes='" + notes + "'" +
            '}';
    }
}
