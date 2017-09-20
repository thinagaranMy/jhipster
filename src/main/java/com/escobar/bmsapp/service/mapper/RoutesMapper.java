package com.escobar.bmsapp.service.mapper;

import com.escobar.bmsapp.domain.*;
import com.escobar.bmsapp.service.dto.RoutesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Routes and its DTO RoutesDTO.
 */
@Mapper(componentModel = "spring", uses = {StationMapper.class, })
public interface RoutesMapper extends EntityMapper <RoutesDTO, Routes> {
    
    
    default Routes fromId(Long id) {
        if (id == null) {
            return null;
        }
        Routes routes = new Routes();
        routes.setId(id);
        return routes;
    }
}
