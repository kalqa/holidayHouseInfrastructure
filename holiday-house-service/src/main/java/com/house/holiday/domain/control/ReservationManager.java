package com.house.holiday.domain.control;

import java.time.LocalDate;

import javax.inject.Inject;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.ReservationResponseDTO.ReservationResponseDTOBuilder;
import com.house.holiday.client.boundary.HolidayHouseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReservationManager {

    @Inject
    HolidayHouseClient holidayHouseClient;

    Logger logger = LoggerFactory.getLogger(ReservationManager.class);

    public ReservationResponseDTO makeReservation(ReservationDTO reservationDto) {
        ReservationResponseDTOBuilder reservationResponseDTOBuilder = ReservationResponseDTO.builder();
        if (!doesRoomWithGivenNumberExist(reservationDto.getRoomNumber())) {
            logger.error("The room with number: {} does not exists", reservationDto.getRoomNumber());
            return reservationResponseDTOBuilder
                    .withMessage(String.format("The room with number: [%s] does not exists", reservationDto.getRoomNumber()))
                    .build();
        }

        if (reservationDto.getFromDate().isAfter(reservationDto.getToDate())) {
            logger.error("Leave date [{}] must be after arrival date [{}]!", reservationDto.getFromDate(), reservationDto.getToDate());
            return reservationResponseDTOBuilder
                    .withMessage(String.format("Leave date [%s] must be after arrival date [%s]!", reservationDto.getFromDate(), reservationDto.getToDate()))
                    .build();
        }

        if (isThereAnyCollisionWithGivenDate(reservationDto)) {
            logger.error("Leave date [{}] must be after arrival date [{}]!", reservationDto.getFromDate(), reservationDto.getToDate());
            return reservationResponseDTOBuilder
                    .withMessage(String.format("Room with number: %s is busy from %s to %s", reservationDto.getRoomNumber(), reservationDto.getFromDate(), reservationDto.getToDate()))
                    .build();
        }

        return holidayHouseClient.makeReservation(reservationDto);
    }

    public ReservationResponseDTO getAllReservationsByNickName(String nickName) {
        logger.info("Getting all reservations for user with nickname: {}", nickName);
        return holidayHouseClient.getAllReservationsByNickName(nickName);
    }

    public ReservationResponseDTO cancelReservationById(String reservationId) {
        logger.info("Canceling reservation with id: {}", reservationId);
        return holidayHouseClient.cancelReservationById(reservationId);
    }

    private boolean isThereAnyCollisionWithGivenDate(ReservationDTO reservationDto) {
        return holidayHouseClient.getAllReservations()
                .getReservationDTOs()
                .values()
                .stream()
                .filter(reservation -> isRoomNumberEqualToClientRoomNumber(reservationDto, reservation))
                .anyMatch(reservation -> isReservationDateBetweenGiven(reservation, reservationDto.getFromDate(), reservationDto.getToDate()));
    }

    private boolean doesRoomWithGivenNumberExist(Integer roomNumber) {
        return holidayHouseClient.getAllRooms().getAvailableRooms()
                .stream()
                .anyMatch(roomDTO -> roomNumber.equals(roomDTO.getRoomNumber()));
    }

    private boolean isRoomNumberEqualToClientRoomNumber(ReservationDTO reservationDto, ReservationDTO reservation) {
        return reservation.getRoomNumber().equals(reservationDto.getRoomNumber());
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