package com.escobar.bmsapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Routes.
 */
@Entity
@Table(name = "routes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "routes")
public class Routes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "routes_transit_stations",
               joinColumns = @JoinColumn(name="routes_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="transit_stations_id", referencedColumnName="id"))
    private Set<Station> transitStations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Routes code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public Routes description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public Routes validTo(LocalDate validTo) {
        this.validTo = validTo;
        return this;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public Routes validFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public Set<Station> getTransitStations() {
        return transitStations;
    }

    public Routes transitStations(Set<Station> stations) {
        this.transitStations = stations;
        return this;
    }

    public Routes addTransitStations(Station station) {
        this.transitStations.add(station);
        station.getTransitRoutes().add(this);
        return this;
    }

    public Routes removeTransitStations(Station station) {
        this.transitStations.remove(station);
        station.getTransitRoutes().remove(this);
        return this;
    }

    public void setTransitStations(Set<Station> stations) {
        this.transitStations = stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Routes routes = (Routes) o;
        if (routes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), routes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Routes{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", validFrom='" + getValidFrom() + "'" +
            "}";
    }
}
