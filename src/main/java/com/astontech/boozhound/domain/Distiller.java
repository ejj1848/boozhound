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
 * A Distiller.
 */
@Entity
@Table(name = "distiller")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "distiller")
public class Distiller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "distiller_name", nullable = false)
    private String distillerName;

    @Column(name = "distiller_rating")
    private Integer distillerRating;

    @OneToOne(mappedBy = "distiller")
    @JsonIgnore
    private CheckIn checkin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistillerName() {
        return distillerName;
    }

    public Distiller distillerName(String distillerName) {
        this.distillerName = distillerName;
        return this;
    }

    public void setDistillerName(String distillerName) {
        this.distillerName = distillerName;
    }

    public Integer getDistillerRating() {
        return distillerRating;
    }

    public Distiller distillerRating(Integer distillerRating) {
        this.distillerRating = distillerRating;
        return this;
    }

    public void setDistillerRating(Integer distillerRating) {
        this.distillerRating = distillerRating;
    }

    public CheckIn getCheckin() {
        return checkin;
    }

    public Distiller checkin(CheckIn checkIn) {
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
        Distiller distiller = (Distiller) o;
        if(distiller.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, distiller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Distiller{" +
            "id=" + id +
            ", distillerName='" + distillerName + "'" +
            ", distillerRating='" + distillerRating + "'" +
            '}';
    }
}
