package com.house.holiday.domain.boundary;

import java.util.Date;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.domain.entity.CancelReservationResponse;
import com.house.holiday.domain.entity.ReservationResponse;
import com.house.holiday.domain.entity.RoomResponse;

public interface HolidayHouseService {

    ReservationResponse makeReservation(ReservationDTO reservationDTO);
    RoomResponse getAvailableRooms(Date fromDate, Date toDate);
    CancelReservationResponse cancelReservationById(String id);
}