package com.escobar.bmsapp.repository;

import com.escobar.bmsapp.domain.Routes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Routes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoutesRepository extends JpaRepository<Routes,Long> {
    
    @Query("select distinct routes from Routes routes left join fetch routes.transitStations")
    List<Routes> findAllWithEagerRelationships();

    @Query("select routes from Routes routes left join fetch routes.transitStations where routes.id =:id")
    Routes findOneWithEagerRelationships(@Param("id") Long id);
    
}
