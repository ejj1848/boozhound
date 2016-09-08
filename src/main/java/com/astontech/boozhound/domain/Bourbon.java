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
 * A Bourbon.
 */
@Entity
@Table(name = "bourbon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bourbon")
public class Bourbon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "bourbon_name", nullable = false)
    private String bourbonName;

    @Column(name = "proof")
    private String proof;

    @Column(name = "year")
    private Integer year;

    @Column(name = "bourbon_rating")
    private Integer bourbonRating;

    @ManyToOne
    private Distiller distiller;

    @OneToOne(mappedBy = "bourbon")
    @JsonIgnore
    private CheckIn checkin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBourbonName() {
        return bourbonName;
    }

    public Bourbon bourbonName(String bourbonName) {
        this.bourbonName = bourbonName;
        return this;
    }

    public void setBourbonName(String bourbonName) {
        this.bourbonName = bourbonName;
    }

    public String getProof() {
        return proof;
    }

    public Bourbon proof(String proof) {
        this.proof = proof;
        return this;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public Integer getYear() {
        return year;
    }

    public Bourbon year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getBourbonRating() {
        return bourbonRating;
    }

    public Bourbon bourbonRating(Integer bourbonRating) {
        this.bourbonRating = bourbonRating;
        return this;
    }

    public void setBourbonRating(Integer bourbonRating) {
        this.bourbonRating = bourbonRating;
    }

    public Distiller getDistiller() {
        return distiller;
    }

    public Bourbon distiller(Distiller distiller) {
        this.distiller = distiller;
        return this;
    }

    public void setDistiller(Distiller distiller) {
        this.distiller = distiller;
    }

    public CheckIn getCheckin() {
        return checkin;
    }

    public Bourbon checkin(CheckIn checkIn) {
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
        Bourbon bourbon = (Bourbon) o;
        if(bourbon.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bourbon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bourbon{" +
            "id=" + id +
            ", bourbonName='" + bourbonName + "'" +
            ", proof='" + proof + "'" +
            ", year='" + year + "'" +
            ", bourbonRating='" + bourbonRating + "'" +
            '}';
    }
}
