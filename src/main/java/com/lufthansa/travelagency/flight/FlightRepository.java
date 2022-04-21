package com.lufthansa.travelagency.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findFlightById(Long id);

    @Query("select f.flight_number from Flight f where f.flight_number = :flight_number")
    Optional<Long> getByFlightNumber(@Param("flight_number") Long flight_number);
}
