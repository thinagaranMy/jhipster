package com.escobar.bmsapp.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Trip entity.
 */
public class TripDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String description;

    @NotNull
    @Min(value = 0)
    private Integer passengerCount;

    @NotNull
    private Instant scheduledTime;

    @NotNull
    private Instant departureTime;

    private Instant arrivalTime;

    @NotNull
    private Boolean activeTrip;

    private Long routesId;

    private String routesCode;

    private Long tripMasterId;

    private String tripMasterLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Boolean isActiveTrip() {
        return activeTrip;
    }

    public void setActiveTrip(Boolean activeTrip) {
        this.activeTrip = activeTrip;
    }

    public Long getRoutesId() {
        return routesId;
    }

    public void setRoutesId(Long routesId) {
        this.routesId = routesId;
    }

    public String getRoutesCode() {
        return routesCode;
    }

    public void setRoutesCode(String routesCode) {
        this.routesCode = routesCode;
    }

    public Long getTripMasterId() {
        return tripMasterId;
    }

    public void setTripMasterId(Long userId) {
        this.tripMasterId = userId;
    }

    public String getTripMasterLogin() {
        return tripMasterLogin;
    }

    public void setTripMasterLogin(String userLogin) {
        this.tripMasterLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TripDTO tripDTO = (TripDTO) o;
        if(tripDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tripDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TripDTO{" +
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
