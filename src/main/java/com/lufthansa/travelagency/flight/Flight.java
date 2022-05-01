package com.lufthansa.travelagency.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.lufthansa.travelagency.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long flight_number;

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

    @Basic
    @Temporal(TemporalType.DATE)
    private Date date_created;

    @OneToOne(mappedBy = "flight")
    @JsonIgnore
    private Trip trip;
}
