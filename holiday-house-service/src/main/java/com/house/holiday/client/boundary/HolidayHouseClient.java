package com.house.holiday.client.boundary;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO;

public interface HolidayHouseClient {

    RoomResponseDTO getAllRooms();
    ReservationResponseDTO getAllReservations();
    ReservationResponseDTO getAllReservationsByNickName(String nickName);
    ReservationResponseDTO makeReservation(ReservationDTO reservationDto);
    ReservationResponseDTO cancelReservationById(String reservationId);
}