package com.escobar.bmsapp.repository;

import com.escobar.bmsapp.domain.Trip;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {

    @Query("select trip from Trip trip where trip.tripMaster.login = ?#{principal.username}")
    List<Trip> findByTripMasterIsCurrentUser();
    
}
