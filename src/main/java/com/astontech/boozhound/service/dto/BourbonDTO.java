package com.astontech.boozhound.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Bourbon entity.
 */
public class BourbonDTO implements Serializable {

    private Long id;

    @NotNull
    private String bourbonName;

    private String proof;

    private Integer year;

    private Integer bourbonRating;


    private Long distillerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getBourbonName() {
        return bourbonName;
    }

    public void setBourbonName(String bourbonName) {
        this.bourbonName = bourbonName;
    }
    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getBourbonRating() {
        return bourbonRating;
    }

    public void setBourbonRating(Integer bourbonRating) {
        this.bourbonRating = bourbonRating;
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

        BourbonDTO bourbonDTO = (BourbonDTO) o;

        if ( ! Objects.equals(id, bourbonDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BourbonDTO{" +
            "id=" + id +
            ", bourbonName='" + bourbonName + "'" +
            ", proof='" + proof + "'" +
            ", year='" + year + "'" +
            ", bourbonRating='" + bourbonRating + "'" +
            '}';
    }
}
