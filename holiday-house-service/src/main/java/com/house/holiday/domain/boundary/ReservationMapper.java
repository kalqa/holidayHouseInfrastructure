package com.house.holiday.domain.boundary;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.domain.entity.Reservation;

public class ReservationMapper {

    public Reservation mapToReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setFromDate(reservationDTO.getFromDate());
        reservation.setToDate(reservationDTO.getToDate());
        reservation.setRoomNumber(reservationDTO.getRoomNumber());
        reservation.setUserName(reservationDTO.getUserName());
        return reservation;
    }
}