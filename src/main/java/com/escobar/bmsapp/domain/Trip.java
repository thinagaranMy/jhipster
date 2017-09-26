package com.escobar.bmsapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "trip")
public class Trip implements Serializable {

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

    @NotNull
    @Min(value = 0)
    @Column(name = "passenger_count", nullable = false)
    private Integer passengerCount;

    @NotNull
    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @NotNull
    @Column(name = "departure_time", nullable = false)
    private Instant departureTime;

    @Column(name = "arrival_time")
    private Instant arrivalTime;

    @NotNull
    @Column(name = "active_trip", nullable = false)
    private Boolean activeTrip;

    @ManyToOne(optional = false)
    @NotNull
    private Routes routes;

    @ManyToOne
    private User tripMaster;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Trip code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public Trip description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public Trip passengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
        return this;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public Trip scheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public Trip departureTime(Instant departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public Trip arrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Boolean isActiveTrip() {
        return activeTrip;
    }

    public Trip activeTrip(Boolean activeTrip) {
        this.activeTrip = activeTrip;
        return this;
    }

    public void setActiveTrip(Boolean activeTrip) {
        this.activeTrip = activeTrip;
    }

    public Routes getRoutes() {
        return routes;
    }

    public Trip routes(Routes routes) {
        this.routes = routes;
        return this;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }

    public User getTripMaster() {
        return tripMaster;
    }

    public Trip tripMaster(User user) {
        this.tripMaster = user;
        return this;
    }

    public void setTripMaster(User user) {
        this.tripMaster = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trip trip = (Trip) o;
        if (trip.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trip.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", passengerCount='" + getPassengerCount() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            ", departureTime='" + getDepartureTime() + "'" +
            ", arrivalTime='" + getArrivalTime() + "'" +
            ", activeTrip='" + isActiveTrip() + "'" +
            "}";
    }
}
