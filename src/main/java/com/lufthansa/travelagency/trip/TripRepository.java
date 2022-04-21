package com.lufthansa.travelagency.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("select t from Trip t where (:trip_status is null or t.trip_status like :trip_status) and (:trip_reason is null or t.trip_reason like :trip_reason)")
    List<Trip> findAllByStatusOrReason(ETripStatus trip_status, ETripReason trip_reason);
}
