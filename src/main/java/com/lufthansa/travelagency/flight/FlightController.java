package com.lufthansa.travelagency.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Flight>> getAll() {
        List<Flight> flight = flightService.findAll();
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(flightService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> create(@Valid @RequestBody FlightDto flightDto) {
        return new ResponseEntity<>(flightService.create(flightDto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> update(@PathVariable("id") Long id, @RequestBody FlightDto flightDto) {
        Flight updateFlight = flightService.update(id, flightDto);
        return new ResponseEntity<>(updateFlight, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        flightService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
