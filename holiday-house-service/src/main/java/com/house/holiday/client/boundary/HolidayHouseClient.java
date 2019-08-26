package com.house.holiday.client.boundary;

import java.util.Collection;
import java.util.Map;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.house.holiday.domain.entity.ReservationResponse;

public interface HolidayHouseClient {

    Map<String, RoomDTO> getAllRooms();
    ReservationResponse makeReservation(ReservationDTO reservationDto);
    String cancelReservation(String reservationId);
    Collection<ReservationDTO> getAllReservations();
}