package com.astontech.boozhound.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Distiller entity.
 */
public class DistillerDTO implements Serializable {

    private Long id;

    @NotNull
    private String distillerName;

    private Integer distillerRating;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDistillerName() {
        return distillerName;
    }

    public void setDistillerName(String distillerName) {
        this.distillerName = distillerName;
    }
    public Integer getDistillerRating() {
        return distillerRating;
    }

    public void setDistillerRating(Integer distillerRating) {
        this.distillerRating = distillerRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DistillerDTO distillerDTO = (DistillerDTO) o;

        if ( ! Objects.equals(id, distillerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DistillerDTO{" +
            "id=" + id +
            ", distillerName='" + distillerName + "'" +
            ", distillerRating='" + distillerRating + "'" +
            '}';
    }
}
