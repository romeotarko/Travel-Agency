package com.lufthansa.travelagency.flight;

import com.lufthansa.travelagency.exception.TripAgencyApplicationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FlightServiceTest {

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;


    @Test
    void should_create_flight() {
        FlightDto flightDto = new FlightDto(
                23423L,
                "Rimini",
                "Tirana",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "testAdmin"
        );

        flightService.create(flightDto);

        List<Flight> allFlights = flightRepository.findAll();

        then(allFlights).isNotEmpty();

    }


    @Test
    void should_fail_to_create_flight_with_same_flight_number() {

        givenFlight();

        FlightDto flightDto = new FlightDto(
                1312L,
                "Rimini",
                "Tirana",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "testAdmin"
        );

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            flightService.create(flightDto);
        });

        assertEquals("Flight with number 1312 already exists", exception.getErrorMessage());

    }

    @Test
    void should_update_trip() {

        FlightDto flightDto = new FlightDto(
                123L,
                "Tirona",
                "Prauge",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "testAdmin");

        Flight existingFlight = flightRepository.saveAndFlush(flightDto.convertToEntity());

        FlightDto toUpdateDto = new FlightDto(
                123L,
                "Tirona",
                "Roma",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "testAdmin");

        Flight updatedFlight = flightService.update(existingFlight.getId(), toUpdateDto);

        then(updatedFlight).isNotNull();
        then(updatedFlight.getFlight_number()).isEqualTo(123);
        then(updatedFlight.getArrival_place()).isEqualTo("Roma");

    }

    @Test
    void should_fail_to_create_flight_when_departure_date_is_after_arrival_date() {

        FlightDto flightDto = new FlightDto(
                223L,
                "Tirona",
                "Prauge",
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                "testAdmin");


        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            flightService.create(flightDto);
        });

        assertEquals("Departure date must be before arrival date", exception.getErrorMessage());

    }

    @Test
    void should_fail_to_create_flight_when_departure_place_is_same_as_arrival_place() {


        FlightDto flightDto = new FlightDto(
                123L,
                "Prauge",
                "Prauge",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "testAdmin");

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            flightService.create(flightDto);
        });

        assertEquals("Departure place and arrival place must be different places", exception.getErrorMessage());

    }

    @Test
    void should_fail_if_flight_exists() {
        Flight existingFlight = givenFlight();

        TripAgencyApplicationException exception = assertThrows(TripAgencyApplicationException.class, () -> {
            flightService.checkIfExists(1432L);

        });
        assertEquals("Flight with id: 1432 does not exist!", exception.getErrorMessage());
    }


    private Flight givenFlight() {
        Flight flight = new Flight();
        flight.setId(122L);
        flight.setArrival_date(LocalDate.now().plusDays(1));
        flight.setFlight_number(1312L);
        flight.setArrival_place("Italy");
        flight.setDeparture_place("Rimini");
        flight.setDeparture_date(LocalDate.now());

        return flightRepository.saveAndFlush(flight);
    }
}