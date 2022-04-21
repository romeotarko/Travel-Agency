package com.lufthansa.travelagency.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/trips")
public class TripController {
    @Autowired
    private TripService tripService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Trip>> search(@RequestBody TripSearchDto tripSearchDto) {
        List<Trip> trips = tripService.search(tripSearchDto);
        return ResponseEntity.ok(trips);
    }

    @PostMapping("/addFlightToTrip/{tripId}/flights/{flightId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Trip> addFlightToTrip(@PathVariable("tripId") Long tripId, @PathVariable("flightId") Long flightId) {
        return ResponseEntity.ok(tripService.addFlightToTrip(tripId, flightId));
    }

    @PostMapping("/removeFlightFromTrip/{tripId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Trip> removeFlightFromTrip(@PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok(tripService.removeFlightFromTrip(tripId));
    }


    @PostMapping("/approveTrip/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveTrip(@PathVariable("id") Long id) {
        tripService.approveTrip(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requestForApproval/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Trip> requestForApproval(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tripService.requestForApproval(id));
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Trip> getById(@PathVariable("id") Long id) {
        Trip trip = tripService.findById(id);
        return ResponseEntity.ok(trip);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Trip> create(@Valid @RequestBody TripDto tripDto) {
        return new ResponseEntity<>(tripService.create(tripDto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Trip> update(@PathVariable("id") Long id, @RequestBody TripDto tripDto) {
        Trip updateTrip = tripService.update(id, tripDto);
        return ResponseEntity.ok(updateTrip);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        tripService.delete(id);
        return ResponseEntity.ok().build();
    }

}
