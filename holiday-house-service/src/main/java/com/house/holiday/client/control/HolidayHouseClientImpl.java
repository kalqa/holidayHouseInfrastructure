package com.house.holiday.client.control;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.ReservationResponseDTO.ReservationResponseDTOBuilder;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO.RoomResponseDTOBuilder;
import com.house.holiday.client.boundary.HolidayHouseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HolidayHouseClientImpl implements HolidayHouseClient {

    private Logger logger = LoggerFactory.getLogger(HolidayHouseClientImpl.class);
    private Client restClient = ClientBuilder.newClient();

    @Override
    public RoomResponseDTO getAllRooms() {
        Response response = null;
        RoomResponseDTOBuilder roomResponseBuilder = RoomResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/rooms.json")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, RoomDTO> stringRoomDTOMap = response.readEntity(new GenericType<Map<String, RoomDTO>>() {});
                roomResponseBuilder.withAvailableRooms(stringRoomDTOMap.values());
            } else {
                roomResponseBuilder.withMessage("Could not get all rooms");
                logger.error("Could not get all rooms. Status from server {}", response.getStatus());
            }
        } catch (Exception e) {
            logger.error("Error while getting all rooms", e);
            roomResponseBuilder.withMessage("Error while getting all rooms");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return roomResponseBuilder.build();
    }

    @Override
    public ReservationResponseDTO makeReservation(ReservationDTO reservationDto) {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponseBuilder = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json")
                    .request()
                    .post(Entity.entity(reservationDto, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                ReservationDTO reservationResponse = response.readEntity(new GenericType<ReservationDTO>() {});
                String id = reservationResponse.getId();
                reservationResponseBuilder.withId(id);
                logger.info("Created reservation {} with id {}", reservationDto, id);
            } else {
                reservationResponseBuilder.withMessage(String.format("Could not make reservation by given room: [%s]", reservationDto.getRoomNumber().toString()));
                logger.error("Could not make reservation: {}. Status from server {}", reservationDto, response.getStatus());
            }
        } catch (Exception e) {
            logger.error("Error while making reservation: {}", reservationDto, e);
            reservationResponseBuilder.withMessage("Error while making reservation");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return reservationResponseBuilder.build();
    }

    @Override
    public ReservationResponseDTO cancelReservationById(String reservationId) {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponseBuilder = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations")
                    .path(reservationId)
                    .path(".json")
                    .request()
                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                reservationResponseBuilder.withId(reservationId).build();
            } else {
                logger.error("Could not cancel reservation with Id: {}. Status from server {}", reservationId, response.getStatus());
                reservationResponseBuilder.withMessage(String.format("Could not cancel reservation by given Id: [%s]", reservationId));
            }
        } catch (Exception e) {
            logger.error("Error while canceling reservation: {}", reservationId, e);
            reservationResponseBuilder.withMessage("Error while canceling reservation");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return reservationResponseBuilder.build();
    }

    @Override
    public ReservationResponseDTO getAllReservations() {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponseBuilder = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json")
                    .request()
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, ReservationDTO> reservationDTOS = response.readEntity(new GenericType<Map<String, ReservationDTO>>() {});
                reservationResponseBuilder.withReservations(reservationDTOS);
            } else {
                logger.error("Could not get all reservations. Server status: {}", response.getStatus());
                reservationResponseBuilder.withMessage(String.format("Could not get all reservations. Server status: %s", response.getStatus()));
            }
        } catch (Exception e) {
            logger.error("Error while getting all reservations", e);
            reservationResponseBuilder.withMessage("Error while getting all reservations");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return reservationResponseBuilder.build();
    }

    @Override
    public ReservationResponseDTO getAllReservationsByNickName(String nickName) {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponseBuilder = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json?orderBy=\"userName\"&equalTo=\"" + nickName + "\"")
                    .request()
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, ReservationDTO> stringReservationDTOMap = response.readEntity(new GenericType<Map<String, ReservationDTO>>() {});
                reservationResponseBuilder.withReservations(stringReservationDTOMap);
            } else {
                logger.error("Error while getting all reservations for nickName. Server status: {}", response.getStatus());
                reservationResponseBuilder.withMessage(String.format("Error while getting all reservations for nickName [%s]", nickName));
            }
        } catch (Exception e) {
            logger.error("Error while getting all reservations for nickName: {}", nickName, e);
            reservationResponseBuilder.withMessage(String.format("Could not get all Reservations by NickName: [%s] error: ", nickName));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return reservationResponseBuilder.build();
    }
}