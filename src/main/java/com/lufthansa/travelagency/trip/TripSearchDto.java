package com.lufthansa.travelagency.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripSearchDto {

    @Enumerated(EnumType.STRING)
    private ETripStatus trip_status;

    @Enumerated(EnumType.STRING)
    private ETripReason trip_reason;
}
