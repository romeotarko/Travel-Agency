package com.lufthansa.travelagency.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.lufthansa.travelagency.trip.ETripStatus.CREATED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripDto {

    @NotBlank
    @Size(max = 120)
    private String description;

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

    @NonNull
    @Enumerated(EnumType.STRING)
    private ETripReason trip_reason;


    public Trip convertToEntity() {
        Trip trip = new Trip();
        trip.setDescription(getDescription());
        trip.setDeparture_place(getDeparture_place());
        trip.setArrival_place(getArrival_place());
        trip.setDeparture_date(getDeparture_date());
        trip.setArrival_date(getArrival_date());
        trip.setTrip_reason(getTrip_reason());
        trip.setTrip_status(CREATED);
        return trip;
    }

    public Trip convertToUpdatedEntity(Trip tripToBeUpdated) {

        tripToBeUpdated.setDescription(getDescription());
        tripToBeUpdated.setDeparture_place(getDeparture_place());
        tripToBeUpdated.setArrival_place(getArrival_place());
        tripToBeUpdated.setDeparture_date(getDeparture_date());
        tripToBeUpdated.setArrival_date(getArrival_date());
        tripToBeUpdated.setTrip_reason(getTrip_reason());

        return tripToBeUpdated;
    }

}
