package com.lufthansa.travelagency.flight;

import com.lufthansa.travelagency.exception.TripAgencyApplicationException;
import com.lufthansa.travelagency.trip.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TripRepository tripRepository;

    public Flight findById(Long id) {
        return flightRepository.findFlightById(id).orElseThrow(() -> new TripAgencyApplicationException("Flight by id" + id + "was not found", HttpStatus.NOT_FOUND));
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Flight create(FlightDto flightDto) {

        if (flightRepository.getByFlightNumber(flightDto.getFlight_number()).isPresent()) {
            log.info("Flight with number {} already exists", flightDto.getFlight_number());
            throw new TripAgencyApplicationException(
                    "Flight with number " + flightDto.getFlight_number() + " already exists", HttpStatus.BAD_REQUEST);
        }

        Flight flight = flightDto.convertToEntity();
        validateFlight(flight);

        return flightRepository.saveAndFlush(flight);
    }

    public Flight update(Long id, FlightDto flightDto) {

        Flight existingFlight = findById(id);

        Flight updatedFlight = flightDto.convertToUpdateEntity(existingFlight);

        validateFlight(updatedFlight);

        return flightRepository.saveAndFlush(updatedFlight);
    }

    public void delete(Long id) {
        checkIfExists(id);
        flightRepository.deleteById(id);
    }

    public void checkIfExists(Long id) {
        if (!flightRepository.existsById(id)) {
            log.debug("Flight with id {} does not exist!", id);
            throw new TripAgencyApplicationException("Flight with id: " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    private static void validateFlight(Flight flight) {
        if (flight.getDeparture_date().isAfter(flight.getArrival_date()) || flight.getDeparture_date().isEqual(flight.getArrival_date())) {
            log.info("Departure date must be before arrival date");
            throw new TripAgencyApplicationException("Departure date must be before arrival date", HttpStatus.BAD_REQUEST);
        }
        if (flight.getDeparture_place().equals(flight.getArrival_place())) {
            log.info("Departure place and must arrival place must be different places");
            throw new TripAgencyApplicationException("Departure place and arrival place must be different places", HttpStatus.BAD_REQUEST);
        }
    }
}
