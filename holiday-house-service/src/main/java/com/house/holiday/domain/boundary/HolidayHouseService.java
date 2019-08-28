package com.house.holiday.domain.boundary;

import java.time.LocalDate;
import java.util.Date;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.domain.entity.CancelReservationResponse;
import com.house.holiday.domain.entity.ReservationResponse;
import com.house.holiday.domain.entity.RoomResponse;

public interface HolidayHouseService {

    ReservationResponse makeReservation(ReservationDTO reservationDTO);
    RoomResponse getAvailableRooms(LocalDate fromDate, LocalDate toDate);
    CancelReservationResponse cancelReservationById(String id);
}