package com.lufthansa.travelagency.trip;

import com.lufthansa.travelagency.exception.TripAgencyApplicationException;
import com.lufthansa.travelagency.flight.Flight;
import com.lufthansa.travelagency.flight.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lufthansa.travelagency.trip.ETripStatus.*;

@Service
@Transactional
@Slf4j
public class TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private FlightService flightService;


    public Trip findById(Long id) {
        return tripRepository.findById(id).orElseThrow(() ->
                new TripAgencyApplicationException("Trip by id" + id + "was not found", HttpStatus.NOT_FOUND));
    }


    public List<Trip> search(TripSearchDto tripSearchDto) {
        return tripRepository.findAllByStatusOrReason(tripSearchDto.getTrip_status(), tripSearchDto.getTrip_reason());
    }

    public Trip create(TripDto tripDto) {
        Trip trip = tripDto.convertToEntity();
        validateTrip(trip);
        return tripRepository.saveAndFlush(trip);
    }

    public Trip update(Long id, TripDto tripDto) {

        Trip existingTrip = findById(id);
        Trip updatedTrip = tripDto.convertToUpdatedEntity(existingTrip);

        validateTrip(updatedTrip);
        return tripRepository.saveAndFlush(updatedTrip);
    }


    public Trip requestForApproval(Long id) {
        Trip trip = findById(id);

        if (!trip.getTrip_status().equals(CREATED)) {
            log.info("Trip status must be CREATED");
            throw new TripAgencyApplicationException("Trip status must be CREATED", HttpStatus.NOT_FOUND);
        }
        trip.setTrip_status(WAITING_FOR_APPROVAL);
        return tripRepository.saveAndFlush(trip);
    }

    public Trip approveTrip(Long id) {
        Trip trip = findById(id);

        if (!trip.getTrip_status().equals(WAITING_FOR_APPROVAL)) {
            log.info("Trip status must be WAITING_FOR_APPROVAL");
            throw new TripAgencyApplicationException("Trip status must be WAITING_FOR_APPROVAL", HttpStatus.BAD_REQUEST);
        }
        trip.setTrip_status(APPROVED);
        return tripRepository.saveAndFlush(trip);
    }

    public Trip addFlightToTrip(Long tripId, Long flightId) {
        Trip trip = findById(tripId);

        Flight flight = flightService.findById(flightId);

        if (!trip.getTrip_status().equals(APPROVED)) {
            log.info("Trip must be on APPROVED status ");
            throw new TripAgencyApplicationException("Trip must be on APPROVED status", HttpStatus.BAD_REQUEST);
        }
        if (!trip.getDeparture_place().equals(flight.getDeparture_place()) || !trip.getArrival_place().equals(flight.getArrival_place())) {
            log.info("Trip doesn't match with Flight they must have the same departure and arrival place");
            throw new TripAgencyApplicationException("Trip doesn't match with Flight they must have the same departure and arrival place", HttpStatus.BAD_REQUEST);
        }
        trip.setFlight(flight);
        return tripRepository.saveAndFlush(trip);
    }

    public Trip removeFlightFromTrip(Long tripId) {
        Trip trip = findById(tripId);

        if (trip.getFlight() == null) {
            log.info("Trip doesn't have flight assigned");
            throw new TripAgencyApplicationException("Trip doesn't have flight assigned", HttpStatus.NOT_FOUND);
        }
        trip.setFlight(null);
        return tripRepository.saveAndFlush(trip);
    }

    public void delete(Long id) {
        Trip trip = findById(id);
        tripRepository.delete(trip);
    }

    private static void validateTrip(Trip trip) {
        if (trip.getDeparture_date().isAfter(trip.getArrival_date())) {
            log.info("Departure date must be before arrival date");
            throw new TripAgencyApplicationException("Departure date must be before arrival date", HttpStatus.BAD_REQUEST);
        }
        if (trip.getDeparture_place().equals(trip.getArrival_place())) {
            log.info("Departure place and must arrival place must be different places");
            throw new TripAgencyApplicationException("Departure place and arrival place must be different places", HttpStatus.BAD_REQUEST);
        }
    }
}
