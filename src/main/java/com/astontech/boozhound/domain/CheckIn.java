package com.astontech.boozhound.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CheckIn.
 */
@Entity
@Table(name = "check_in")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "checkin")
public class CheckIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "notes")
    private String notes;


    @ManyToOne
    private Location location;

    @ManyToOne
    private Distiller distiller;

    @ManyToOne
    private Bourbon bourbon;

    @ManyToOne
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public CheckIn notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Location getLocation() {
        return location;
    }

    public CheckIn location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Distiller getDistiller() {
        return distiller;
    }

    public CheckIn distiller(Distiller distiller) {
        this.distiller = distiller;
        return this;
    }

    public void setDistiller(Distiller distiller) {
        this.distiller = distiller;
    }

    public Bourbon getBourbon() {
        return bourbon;
    }

    public CheckIn bourbon(Bourbon bourbon) {
        this.bourbon = bourbon;
        return this;
    }

    public void setBourbon(Bourbon bourbon) {
        this.bourbon = bourbon;
    }

    public Person getPerson() {
        return person;
    }

    public CheckIn person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CheckIn checkIn = (CheckIn) o;
        if(checkIn.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checkIn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CheckIn{" +
            "id=" + id +
            ", notes='" + notes + "'" +
            '}';
    }
}
