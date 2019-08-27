package com.house.holiday.domain.control;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.house.holiday.client.control.HolidayHouseClientImpl;
import com.house.holiday.domain.boundary.HolidayHouseService;
import com.house.holiday.domain.boundary.ReservationMapper;
import com.house.holiday.domain.boundary.RoomMapper;
import com.house.holiday.domain.entity.CancelReservationResponse;
import com.house.holiday.domain.entity.Reservation;
import com.house.holiday.domain.entity.ReservationResponse;
import com.house.holiday.domain.entity.Room;
import com.house.holiday.domain.entity.RoomResponse;

public class HolidayHouseServiceImpl implements HolidayHouseService {

    @Inject
    HolidayHouseClientImpl holidayHouseClient;

    @Inject
    RoomMapper roomMapper;

    @Inject
    ReservationMapper reservationMapper;

    @Override
    public ReservationResponse makeReservation(ReservationDTO reservationDto) {
        boolean isThereAtLeastOneCollision = holidayHouseClient.getAllReservations()
                .stream()
                .map(reservationDTO -> reservationMapper.mapToReservation(reservationDTO))
                .filter(reservation -> reservation.getRoomNumber().equals(reservationDto.getRoomNumber()))
                .anyMatch(reservation -> !isReservationNotAvailable(reservation, reservationDto.getFromDate(), reservationDto.getToDate()));

        if (isThereAtLeastOneCollision) {
            return ReservationResponse.builder().withMessage("please choose another period").build();
        }

        return holidayHouseClient.makeReservation(reservationDto);
    }

    @Override
    public RoomResponse getAvailableRooms(Date fromDate, Date toDate) {
        Collection<RoomDTO> allRooms = holidayHouseClient.getAllRooms().values();
        List<Room> availableRooms = getAvailableRooms(fromDate, toDate, allRooms);

        return RoomResponse.builder()
                .withAvailableRooms(availableRooms)
                .build();
    }

    @Override
    public CancelReservationResponse cancelReservationById(String reservationId) {
        String id = holidayHouseClient.cancelReservation(reservationId);

        return CancelReservationResponse.builder()
                .withId(id)
                .build();
    }

    private List<Room> getAvailableRooms(Date fromDate, Date toDate, Collection<RoomDTO> allRooms) {
        return allRooms.stream()
                .filter(room -> getAvailableRoomsForPeriod(fromDate, toDate)
                        .contains(room.getRoomNumber()))
                .map(roomDTO -> roomMapper.mapToRoom(roomDTO))
                .collect(Collectors.toList());
    }

    private List<Integer> getAvailableRoomsForPeriod(Date fromDate, Date toDate) {
        return holidayHouseClient.getAllReservations().stream()
                .map(reservationDTO -> reservationMapper.mapToReservation(reservationDTO))
                .filter(reservation -> isReservationNotAvailable(reservation, fromDate, toDate))
                .map(Reservation::getRoomNumber)
                .collect(Collectors.toList());
    }

    private boolean isReservationNotAvailable(Reservation reservation, Date clientFromDate, Date clientToDate) {
        Date fromDate = reservation.getFromDate();
        Date toDate = reservation.getToDate();
        return !(isDataInRange(clientFromDate, fromDate, toDate) || isDataInRange(clientToDate, fromDate, toDate));
    }

    private boolean isDataInRange(Date clientFromDate, Date fromDate, Date toDate) {
        return !(clientFromDate.before(fromDate) || clientFromDate.after(toDate));
    }
}