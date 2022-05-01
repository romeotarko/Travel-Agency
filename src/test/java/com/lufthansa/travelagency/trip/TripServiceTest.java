package com.lufthansa.travelagency.trip;

import com.lufthansa.travelagency.exception.TripAgencyApplicationException;
import com.lufthansa.travelagency.flight.Flight;
import com.lufthansa.travelagency.flight.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.lufthansa.travelagency.trip.ETripStatus.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TripServiceTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    void should_create_trip() {
        TripDto tripDto = new TripDto(
                "test trip",
                "Tirona",
                "Prague",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                ETripReason.EVENT,
                "testUser");

        tripService.create(tripDto);

        List<Trip> allTrip = tripRepository.findAll();

        then(allTrip).isNotEmpty();

    }


    @Test
    void should_fail_to_create_trip_when_departure_date_is_after_arrival_date() {

        TripDto tripDto = new TripDto(
                "test trip",
                "Tirona",
                "Prague",
                LocalDate.now(),
                LocalDate.now().minusDays(1),
                ETripReason.EVENT,
                "testUser");

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            tripService.create(tripDto);
        });

        assertEquals("Departure date must be before arrival date", exception.getErrorMessage());

    }

    @Test
    void should_fail_to_create_trip_when_departure_place_is_same_as_arrival_place() {

        TripDto tripDto = new TripDto(
                "test trip",
                "Tirona",
                "Tirona",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                ETripReason.EVENT,
                "testUser");

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            tripService.create(tripDto);
        });

        assertEquals("Departure place and arrival place must be different places", exception.getErrorMessage());

    }

    @Test
    void should_update_trip() {

        TripDto tripDto = new TripDto(
                "test trip",
                "Tirona",
                "Prauge",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                ETripReason.EVENT,
                "testUser");

        Trip existingTrip = tripRepository.saveAndFlush(tripDto.convertToEntity());

        TripDto toUpdateDto = new TripDto(
                "update description",
                "Tirona",
                "Roma",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                ETripReason.EVENT,
                "testUser");

        Trip updatedTrip = tripService.update(existingTrip.getId(), toUpdateDto);

        then(updatedTrip).isNotNull();
        then(updatedTrip.getDescription()).isEqualTo("update description");
        then(updatedTrip.getArrival_place()).isEqualTo("Roma");

    }

    @Test
    void should_change_trip_status_to_waiting_for_approval() {

        Trip existingTrip = givenTrip(CREATED);

        Trip updatedTrip = tripService.requestForApproval(existingTrip.getId());

        then(updatedTrip).isNotNull();
        then(updatedTrip.getTrip_status()).isEqualTo(WAITING_FOR_APPROVAL);
    }


    @Test
    void should_fail_when_trip_status_is_not_on_status_created() {

        Trip existingTrip = givenTrip(APPROVED);

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            tripService.requestForApproval(existingTrip.getId());
        });

        assertEquals("Trip status must be CREATED", exception.getErrorMessage());
    }

    @Test
    void should_change_trip_status_to_approve() {

        Trip existingTrip = givenTrip(WAITING_FOR_APPROVAL);

        Trip updatedTrip = tripService.approveTrip(existingTrip.getId());

        then(updatedTrip).isNotNull();
        then(updatedTrip.getTrip_status()).isEqualTo(APPROVED);
    }

    @Test
    void should_add_flight_to_trip() {

        Trip existingTrip = givenTrip(APPROVED);

        Flight existingFlight = givenFlight("Tirona");

        Trip updatedTrip = tripService.addFlightToTrip(existingTrip.getId(), existingFlight.getId());

        then(updatedTrip.getFlight()).isNotNull();
    }

    @Test
    void should_fail_to_add_flight_to_trip_when_status_is_incorrect() {

        Trip existingTrip = givenTrip(CREATED);
        Flight existingFlight = givenFlight("Tirona");

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            tripService.addFlightToTrip(existingTrip.getId(), existingFlight.getId());
        });

        assertEquals("Trip must be on APPROVED status", exception.getErrorMessage());
    }

    @Test
    void should_fail_to_add_flight_to_trip_when_departure_place_is_different() {

        Trip existingTrip = givenTrip(APPROVED);
        Flight existingFlight = givenFlight("Napoli");

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            tripService.addFlightToTrip(existingTrip.getId(), existingFlight.getId());
        });

        assertEquals("Trip doesn't match with Flight they must have the same departure and arrival place", exception.getErrorMessage());
    }


    @Test
    void should_remove_flight_from_trip() {
        Flight existingFlight = givenFlight("Tirona");
        Trip existingTrip = givenTrip(APPROVED);
        tripService.addFlightToTrip(existingTrip.getId(), existingFlight.getId());

        Trip updatedTrip = tripService.removeFlightFromTrip(existingTrip.getId());

        then(updatedTrip.getFlight()).isNull();
    }

    private Flight givenFlight(String departure_place) {
        Flight flight = new Flight();
        flight.setArrival_date(LocalDate.now().plusDays(1));
        flight.setFlight_number(1312L);
        flight.setArrival_place("Italy");
        flight.setDeparture_place(departure_place);
        flight.setDeparture_date(LocalDate.now());

        return flightRepository.saveAndFlush(flight);
    }

    private Trip givenTrip(ETripStatus status) {
        Trip trip = new Trip();

        trip.setDescription("test trip");
        trip.setDeparture_place("Tirona");
        trip.setArrival_place("Italy");
        trip.setDeparture_date(LocalDate.now());
        trip.setArrival_date(LocalDate.now().plusDays(1));
        trip.setTrip_reason(ETripReason.EVENT);
        trip.setTrip_status(status);

        return tripRepository.saveAndFlush(trip);
    }

}