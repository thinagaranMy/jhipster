package com.escobar.bmsapp.repository;

import com.escobar.bmsapp.domain.UserRole;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the UserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    
//    @Query("select distinct user_role from UserRole user_role left join fetch user_role.user_userRoles")
//    List<UserRole> findAllWithEagerRelationships();
//
//    @Query("select user_role from UserRole user_role left join fetch user_role.user_userRoles where user_role.id =:id")
//    UserRole findOneWithEagerRelationships(@Param("id") Long id);
    
}
