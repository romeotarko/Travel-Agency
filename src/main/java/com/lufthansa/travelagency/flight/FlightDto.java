package com.lufthansa.travelagency.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {

    @NotNull
    private Long flight_number;

    @NotBlank
    @Size(max = 50)
    private String departure_place;

    @NotBlank
    @Size(max = 50)
    private String arrival_place;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "departure_date is required")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate departure_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "arrival_date is required")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate arrival_date;

    @NotBlank
    @Size(max = 50)
    public String created_by;

    public Flight convertToEntity() {
        Flight flight = new Flight();
        flight.setFlight_number(getFlight_number());
        flight.setDeparture_place(getDeparture_place());
        flight.setArrival_place(getArrival_place());
        flight.setDeparture_date(getDeparture_date());
        flight.setArrival_date(getArrival_date());
        flight.setCreated_by(getCreated_by());

        return flight;
    }

    public Flight convertToUpdateEntity(Flight flightToBeUpdated) {

        flightToBeUpdated.setFlight_number(getFlight_number());
        flightToBeUpdated.setDeparture_place(getDeparture_place());
        flightToBeUpdated.setArrival_place(getArrival_place());
        flightToBeUpdated.setDeparture_date(getDeparture_date());
        flightToBeUpdated.setArrival_date(getArrival_date());

        return flightToBeUpdated;
    }
}
