package com.house.holiday.domain.control;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.ReservationResponseDTO.ReservationResponseDTOBuilder;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.client.boundary.HolidayHouseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReservationManager {

    @Inject
    HolidayHouseClient holidayHouseClient;

    private Logger logger = LoggerFactory.getLogger(ReservationManager.class);

    public ReservationResponseDTO makeReservation(ReservationDTO reservationDto) {
        ReservationResponseDTOBuilder reservationResponseDTOBuilder = ReservationResponseDTO.builder();

        Integer reservationRoomNumber = reservationDto.getRoomNumber();
        if (!doesRoomWithGivenNumberExist(reservationRoomNumber)) {
            logger.error("The room with number: {} does not exists", reservationRoomNumber);
            return buildRoomDoesntExistResponse(reservationResponseDTOBuilder, reservationRoomNumber);
        }

        LocalDate reservationFromDate = reservationDto.getFromDate();
        LocalDate reservationToDate = reservationDto.getToDate();
        if (isArrivalDateAfterLeaveDate(reservationDto)) {
            logger.error("Leave date [{}] must be after arrival date [{}]!", reservationFromDate, reservationToDate);
            return buildArrivalDateAfterLeaveDataResponse(reservationResponseDTOBuilder, reservationFromDate, reservationToDate);
        }

        if (isThereAnyCollisionWithGivenDate(reservationDto)) {
            logger.error("Room with number {} is busy from {} to {}. For reservation {}", reservationRoomNumber, reservationFromDate, reservationToDate, reservationDto);
            return buildReservationCollisionResponse(reservationResponseDTOBuilder, reservationRoomNumber, reservationFromDate, reservationToDate);
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

    private ReservationResponseDTO buildReservationCollisionResponse(ReservationResponseDTOBuilder reservationResponseDTOBuilder, Integer reservationRoomNumber, LocalDate reservationFromDate, LocalDate reservationToDate) {
        return buildReservationResponse(reservationResponseDTOBuilder, String.format("Room with number: %s is busy from %s to %s", reservationRoomNumber, reservationFromDate, reservationToDate));
    }

    private ReservationResponseDTO buildArrivalDateAfterLeaveDataResponse(ReservationResponseDTOBuilder reservationResponseDTOBuilder, LocalDate reservationFromDate, LocalDate reservationToDate) {
        return buildReservationResponse(reservationResponseDTOBuilder, String.format("Leave date [%s] must be after arrival date [%s]!", reservationFromDate, reservationToDate));
    }

    private ReservationResponseDTO buildRoomDoesntExistResponse(ReservationResponseDTOBuilder reservationResponseDTOBuilder, Integer reservationRoomNumber) {
        return buildReservationResponse(reservationResponseDTOBuilder, String.format("The room with number: [%s] does not exists", reservationRoomNumber));
    }

    private ReservationResponseDTO buildReservationResponse(ReservationResponseDTOBuilder builder, String message) {
        return builder.withMessage(message).build();
    }

    private boolean isThereAnyCollisionWithGivenDate(ReservationDTO reservationDto) {
        ReservationResponseDTO allReservations = holidayHouseClient.getAllReservations();
        Map<String, ReservationDTO> reservationDTOs = allReservations.getReservationDTOs();

        if (reservationDTOs.size() == 0) {
            return false;
        }

        return isThereAnyReservationCollision(reservationDto, reservationDTOs);
    }

    private boolean isThereAnyReservationCollision(ReservationDTO reservationDto, Map<String, ReservationDTO> reservationDTOs) {
        return reservationDTOs
                .values()
                .stream()
                .filter(reservation -> isRoomNumberEqualToClientRoomNumber(reservationDto, reservation))
                .anyMatch(reservation -> isReservationDateBetweenGiven(reservation, reservationDto.getFromDate(), reservationDto.getToDate()));
    }

    private boolean doesRoomWithGivenNumberExist(Integer roomNumber) {
        RoomResponseDTO allRooms = holidayHouseClient.getAllRooms();
        Collection<RoomDTO> availableRooms = allRooms.getAvailableRooms();

        if (availableRooms.size() == 0) {
            return false;
        }

        return availableRooms.stream().anyMatch(roomDTO -> roomNumber.equals(roomDTO.getRoomNumber()));
    }

    private boolean isArrivalDateAfterLeaveDate(ReservationDTO reservationDto) {
        return reservationDto.getFromDate().isAfter(reservationDto.getToDate());
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