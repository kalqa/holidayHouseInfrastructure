package com.house.holiday.client.control;

import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationDTO.ReservationDTOBuilder;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.ReservationResponseDTO.ReservationResponseDTOBuilder;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO.RoomResponseDTOBuilder;
import com.house.holiday.client.boundary.HolidayHouseClient;

public class HolidayHouseClientImpl implements HolidayHouseClient {

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
                //logger
//                roomResponseBuilder.withMessage("failed"); // TODO ADD MESSAGE TO RESPONSES !!!
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            String message = e.getMessage();
            System.out.println(message);
//            roomResponseBuilder.withMessage(message);  // TODO ADD MESSAGE TO RESPONSES !!!
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return roomResponseBuilder.build();
    }

    @Override
    public ReservationDTO makeReservation(ReservationDTO reservationDto) {
        Response response = null;
        ReservationDTOBuilder reservationResponse = ReservationDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json")
                    .request()
                    .post(Entity.entity(reservationDto, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                ReservationDTO reservationResponseDto = response.readEntity(new GenericType<ReservationDTO>() {});
                reservationResponse.withId(reservationResponseDto.getId());
            } else {
                //logger
                String message = String.format("Could not make reservation by given for room: [%s]", reservationDto.getRoomNumber().toString());
                System.out.println(message);
                reservationResponse.withId(message);
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            String message = e.getMessage();
            System.out.println(message);
            reservationResponse.withId(message);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }

    @Override
    public ReservationResponseDTO cancelReservationById(String reservationId) {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponse = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations")
                    .path(reservationId)
                    .path(".json")
                    .request()
                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                reservationResponse.withId(reservationId).build();
            } else {
                //logger
                String message = String.format("Could not cancel reservation by given Id:: [%s]", reservationId);
                System.out.println(message);
                reservationResponse.withId(message);
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            String message = e.getMessage();
            System.out.println(message);
            reservationResponse.withId(message);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }

    @Override
    public ReservationResponseDTO getAllReservations() {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponse = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json")
                    .request()
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, ReservationDTO> reservationDTOS = response.readEntity(new GenericType<Map<String, ReservationDTO>>() {});
                reservationResponse.withReservations(reservationDTOS);
            } else {
                //logger
                reservationResponse.withId("failed");
            }
        } catch (Exception e) {
            //logger
            e.printStackTrace();
            String message = "Could not get all Reservations." + e.getMessage();
            System.out.println(message);
            reservationResponse.withId(message);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }

    @Override
    public ReservationResponseDTO getAllReservationsByNickName(String nickName) {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponse = ReservationResponseDTO.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json?orderBy=\"userName\"&equalTo=\"" + nickName + "\"")
                    .request()
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, ReservationDTO> stringReservationDTOMap = response.readEntity(new GenericType<Map<String, ReservationDTO>>() {});
                reservationResponse.withReservations(stringReservationDTOMap);
            } else {
                //logger
                reservationResponse.withId("failed");
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            String message = "Could not get all Reservations by NickName: [" + nickName + "] error: " + e.getMessage();
            System.out.println(message);
            reservationResponse.withId(message);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }
}