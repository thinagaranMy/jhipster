package com.escobar.bmsapp.service.mapper;

import com.escobar.bmsapp.domain.*;
import com.escobar.bmsapp.service.dto.UserRoleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserRole and its DTO UserRoleDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface UserRoleMapper extends EntityMapper <UserRoleDTO, UserRole> {
    
    
    default UserRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRole userRole = new UserRole();
        userRole.setId(id);
        return userRole;
    }
}
