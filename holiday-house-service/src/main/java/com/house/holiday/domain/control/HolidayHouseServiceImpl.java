package com.house.holiday.domain.control;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.client.control.HolidayHouseClientImpl;
import com.house.holiday.domain.boundary.HolidayHouseService;

public class HolidayHouseServiceImpl implements HolidayHouseService {

    @Inject
    HolidayHouseClientImpl holidayHouseClient;

    @Override
    public ReservationDTO makeReservation(ReservationDTO reservationDto) {
        boolean isThereAtLeastOneCollision = holidayHouseClient.getAllReservations()
                .getReservationDTOs()
                .values()
                .stream()
                .filter(reservation -> reservation.getRoomNumber().equals(reservationDto.getRoomNumber()))
                .anyMatch(reservation -> !isReservationNotAvailable(reservation, reservationDto.getFromDate(), reservationDto.getToDate()));

        if (isThereAtLeastOneCollision) {
            //logger
            return ReservationDTO.builder()
                    .withId("please choose another period")
                    .build(); // TODO maybe change add message field in reservationDTO
        }

        return holidayHouseClient.makeReservation(reservationDto);
    }

    @Override
    public RoomResponseDTO getAvailableRooms(LocalDate fromDate, LocalDate toDate) {
        Collection<RoomDTO> allRooms = holidayHouseClient.getAllRooms().getAvailableRooms();
        List<RoomDTO> availableRooms = getAvailableRooms(fromDate, toDate, allRooms);

        return RoomResponseDTO.builder()
                .withAvailableRooms(availableRooms)
                .build();
    }

    @Override
    public ReservationResponseDTO cancelReservationById(String reservationId) {
        return holidayHouseClient.cancelReservationById(reservationId);
    }

    @Override
    public ReservationResponseDTO getAllReservationsByNickName(String nickName) {
        return holidayHouseClient.getAllReservationsByNickName(nickName);
    }

    private List<RoomDTO> getAvailableRooms(LocalDate fromDate, LocalDate toDate, Collection<RoomDTO> allRooms) {
        return allRooms.stream()
                .filter(room -> getAvailableRoomsForPeriod(fromDate, toDate)
                        .contains(room.getRoomNumber()))
                .collect(Collectors.toList());
    }

    private List<Integer> getAvailableRoomsForPeriod(LocalDate fromDate, LocalDate toDate) {
        return holidayHouseClient.getAllReservations()
                .getReservationDTOs()
                .values()
                .stream()
                .filter(reservation -> isReservationNotAvailable(reservation, fromDate, toDate))
                .map(ReservationDTO::getRoomNumber)
                .collect(Collectors.toList());
    }

    private boolean isReservationNotAvailable(ReservationDTO reservation, LocalDate clientFromDate, LocalDate clientToDate) {
        LocalDate fromDate = reservation.getFromDate();
        LocalDate toDate = reservation.getToDate();
        return !(isDataInRange(clientFromDate, fromDate, toDate) || isDataInRange(clientToDate, fromDate, toDate));
    }

    private boolean isDataInRange(LocalDate clientFromDate, LocalDate fromDate, LocalDate toDate) {
        return !(clientFromDate.isBefore(fromDate) || clientFromDate.isAfter(toDate));
    }
}