package com.house.holiday.domain.boundary;

import java.time.LocalDate;
import java.util.Collection;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.domain.entity.RoomResponse;

public interface HolidayHouseService {

    ReservationDTO makeReservation(ReservationDTO reservationDTO);
    RoomResponse getAvailableRooms(LocalDate fromDate, LocalDate toDate);
    ReservationDTO cancelReservationById(String id);
    Collection<ReservationDTO> getAllReservationsByNickName(String nickName);
}