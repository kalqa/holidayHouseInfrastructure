package com.house.holiday.domain.boundary;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO;

public interface HolidayHouseService {

    ReservationResponseDTO makeReservation(ReservationDTO reservationDTO);
    RoomResponseDTO getAvailableRooms(String fromDate, String toDate);
    ReservationResponseDTO cancelReservationById(String id);
    ReservationResponseDTO getAllReservationsByNickName(String nickName);
}