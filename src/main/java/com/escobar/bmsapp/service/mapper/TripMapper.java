package com.escobar.bmsapp.service.mapper;

import com.escobar.bmsapp.domain.*;
import com.escobar.bmsapp.service.dto.TripDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Trip and its DTO TripDTO.
 */
@Mapper(componentModel = "spring", uses = {RoutesMapper.class, UserMapper.class, })
public interface TripMapper extends EntityMapper <TripDTO, Trip> {

    @Mapping(source = "routes.id", target = "routesId")
    @Mapping(source = "routes.code", target = "routesCode")

    @Mapping(source = "tripMaster.id", target = "tripMasterId")
    @Mapping(source = "tripMaster.login", target = "tripMasterLogin")
    TripDTO toDto(Trip trip); 

    @Mapping(source = "routesId", target = "routes")

    @Mapping(source = "tripMasterId", target = "tripMaster")
    Trip toEntity(TripDTO tripDTO); 
    default Trip fromId(Long id) {
        if (id == null) {
            return null;
        }
        Trip trip = new Trip();
        trip.setId(id);
        return trip;
    }
}
