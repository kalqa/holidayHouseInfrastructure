package com.house.holiday.reminder.client.boundary;

import java.util.Collection;

import com.holiday.house.api.dto.ReservationDTO;

public interface ReservationClient {

    Collection<ReservationDTO> getAllReservations();
}