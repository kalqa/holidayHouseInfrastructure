package com.house.holiday.client.control;

import java.util.Collection;
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
import com.house.holiday.client.boundary.HolidayHouseClient;

public class HolidayHouseClientImpl implements HolidayHouseClient {

    private Client restClient = ClientBuilder.newClient();

    @Override
    public Map<String, RoomDTO> getAllRooms() {
        return restClient
                .target("https://houseinfrastructurev1.firebaseio.com/rooms.json")
                .request(MediaType.APPLICATION_JSON)
                .get()
                .readEntity(new GenericType<Map<String, RoomDTO>>() {});
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
                reservationResponse.withId("failed");
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            System.out.println("content of the message cannot be mapped to an entity of the requested type" + e.getMessage());
            reservationResponse.withId("Content of the message cannot be mapped to an entity of the requested type |" + e.getMessage());
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
        ReservationResponseDTOBuilder reservationResponse = ReservationResponseDTOBuilder.aReservationResponseDTO();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations")
                    .path(reservationId)
                    .path(".json")
                    .request()
                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return ReservationResponseDTO.ReservationResponseDTOBuilder.aReservationResponseDTO().withId(reservationId).build();
            } else {
                //logger
                reservationResponse.withId("failed");
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            System.out.println("content of the message cannot be mapped to an entity of the requested type" + e.getMessage());
            reservationResponse.withId("Content of the message cannot be mapped to an entity of the requested type |" + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }

    @Override
    public Collection<ReservationDTO> getAllReservations() {
        return restClient
                .target("https://houseinfrastructurev1.firebaseio.com/reservations.json?")
                .request()
                .get()
                .readEntity(new GenericType<Map<String, ReservationDTO>>() {})
                .values();
    }

    @Override
    public ReservationResponseDTO getAllReservationsByNickName(String nickName) {
        Response response = null;
        ReservationResponseDTOBuilder reservationResponse = ReservationResponseDTOBuilder.aReservationResponseDTO();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json?orderBy=\"userName\"&equalTo=\"" + nickName + "\"")
                    .request()
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Map<String, ReservationDTO> stringReservationDTOMap = response.readEntity(new GenericType<Map<String, ReservationDTO>>() {});
                return reservationResponse
                        .withReservations(stringReservationDTOMap).build();
            } else {
                //logger
                reservationResponse.withId("failed");
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            System.out.println("content of the message cannot be mapped to an entity of the requested type" + e.getMessage());
            reservationResponse.withId("Content of the message cannot be mapped to an entity of the requested type |" + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }
}