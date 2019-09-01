package com.house.holiday.domain.control;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.client.boundary.HolidayHouseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomManager {

    Logger logger = LoggerFactory.getLogger(RoomManager.class);

    @Inject
    HolidayHouseClient holidayHouseClient;

    public RoomResponseDTO getAvailableRooms(LocalDate fromDate, LocalDate toDate) {
        logger.info("Getting availableRooms from {} to: {}", fromDate, toDate);
        Set<Integer> allRoomsNumbers = getAllRoomNumbers();
        Set<Integer> roomNumberThatCollide = getRoomNumbersThatCollide(fromDate, toDate);

        try {
            allRoomsNumbers.removeAll(roomNumberThatCollide);
        } catch (Exception e) {
            logger.error("Removing {} from {} failed", allRoomsNumbers, roomNumberThatCollide);
        }

        return RoomResponseDTO.builder()
                .withAvailableRooms(getAvailableRoomsDTO(allRoomsNumbers))
                .build();
    }

    private List<RoomDTO> getAvailableRoomsDTO(Set<Integer> allRoomsNumbers) {
        return allRoomsNumbers
                .stream()
                .map(roomNumber -> RoomDTO.builder().withRoomNumber(roomNumber).build())
                .collect(Collectors.toList());
    }

    private Set<Integer> getRoomNumbersThatCollide(LocalDate fromDate, LocalDate toDate) {
        return holidayHouseClient.getAllReservations().getReservationDTOs().values()
                .stream()
                .filter(reservationDTO -> isReservationDateBetweenGiven(reservationDTO, fromDate, toDate))
                .map(ReservationDTO::getRoomNumber)
                .collect(Collectors.toSet());
    }

    private Set<Integer> getAllRoomNumbers() {
        return holidayHouseClient.getAllRooms().getAvailableRooms().stream()
                .map(RoomDTO::getRoomNumber)
                .collect(Collectors.toSet());
    }

    private boolean isReservationDateBetweenGiven(ReservationDTO reservation, LocalDate clientFromDate, LocalDate clientToDate) {
        LocalDate fromDate = reservation.getFromDate();
        LocalDate toDate = reservation.getToDate();
        return (isDataInRange(clientFromDate, fromDate, toDate) || isDataInRange(clientToDate, fromDate, toDate));
    }

    private boolean isDataInRange(LocalDate clientFromDate, LocalDate fromDate, LocalDate toDate) {
        return !(clientFromDate.isBefore(fromDate) || clientFromDate.isAfter(toDate));
    }
}