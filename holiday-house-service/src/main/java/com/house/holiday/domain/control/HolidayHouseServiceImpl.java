package com.house.holiday.domain.control;

import javax.inject.Inject;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.domain.boundary.HolidayHouseService;

public class HolidayHouseServiceImpl implements HolidayHouseService {

    @Inject
    RoomManager roomManager;

    @Inject
    ReservationManager reservationManager;

    @Override
    public ReservationResponseDTO makeReservation(ReservationDTO reservationDto) {
        return reservationManager.makeReservation(reservationDto);
    }

    @Override
    public RoomResponseDTO getAvailableRooms(String fromDate, String toDate) {
        return roomManager.getAvailableRooms(fromDate, toDate);
    }

    @Override
    public ReservationResponseDTO cancelReservationById(String reservationId) {
        return reservationManager.cancelReservationById(reservationId);
    }

    @Override
    public ReservationResponseDTO getAllReservationsByNickName(String nickName) {
        return reservationManager.getAllReservationsByNickName(nickName);
    }
}