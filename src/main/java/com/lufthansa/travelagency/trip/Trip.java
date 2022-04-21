package com.lufthansa.travelagency.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.lufthansa.travelagency.flight.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 120)
    private String description;

    @Size(max = 50)
    private String departure_place;

    @Size(max = 50)
    private String arrival_place;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "departure_date is required")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departure_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "arrival_date is required")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrival_date;

    @Enumerated(EnumType.STRING)
    private ETripStatus trip_status;

    @Enumerated(EnumType.STRING)
    private ETripReason trip_reason;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    private Flight flight;
}
