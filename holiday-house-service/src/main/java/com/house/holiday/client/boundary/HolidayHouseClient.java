package com.house.holiday.client.boundary;

import java.util.Collection;
import java.util.Map;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.RoomDTO;

public interface HolidayHouseClient {

    Map<String, RoomDTO> getAllRooms();
    ReservationDTO makeReservation(ReservationDTO reservationDto);
    ReservationDTO cancelReservationById(String reservationId);
    Collection<ReservationDTO> getAllReservations();
    Collection<ReservationDTO> getAllReservationsByNickName(String nickName);
}