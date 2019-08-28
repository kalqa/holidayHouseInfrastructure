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
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.house.holiday.client.boundary.HolidayHouseClient;
import com.house.holiday.domain.entity.ReservationResponse;
import com.house.holiday.domain.entity.ReservationResponse.ReservationResponseBuilder;

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
    public ReservationResponse makeReservation(ReservationDTO reservationDto) {
        Response response = null;
        ReservationResponseBuilder reservationResponse = ReservationResponse.builder();

        try {
            response = restClient
                    .target("https://houseinfrastructurev1.firebaseio.com/reservations.json")
                    .request()
                    .post(Entity.entity(reservationDto, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                ReservationResponseDTO reservationResponseDto = response.readEntity(new GenericType<ReservationResponseDTO>() {});
                reservationResponse.withReservationId(reservationResponseDto.getId());
            } else {
                //logger
                reservationResponse.withMessage("failed");
            }
        } catch (ProcessingException e) {
            //logger
            e.printStackTrace();
            System.out.println("content of the message cannot be mapped to an entity of the requested type" + e.getMessage());
            reservationResponse.withMessage("Content of the message cannot be mapped to an entity of the requested type |" + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return reservationResponse.build();
    }

    @Override
    public String cancelReservation(String reservationId) {
        return null;
    }

    @Override
    public Collection<ReservationDTO> getAllReservations() {
        Collection<ReservationDTO> values = restClient
//                .target("https://houseinfrastructurev1.firebaseio.com/reservations.json?orderBy=\"fromDate\"&startAt=\"" + startDate + "\"")
                .target("https://houseinfrastructurev1.firebaseio.com/reservations.json?")
                .request()
                .get()
                .readEntity(new GenericType<Map<String, ReservationDTO>>() {})
                .values();
        return values;
    }
}