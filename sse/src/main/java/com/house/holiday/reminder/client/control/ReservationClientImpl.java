package com.house.holiday.reminder.client.control;

import java.util.Collection;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.reminder.client.boundary.ReservationClient;

@Component
public class ReservationClientImpl implements ReservationClient {

    @Override
    public Collection<ReservationDTO> getAllReservations() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, ReservationDTO>> response = restTemplate.exchange(
                "https://houseinfrastructurev1.firebaseio.com/reservations.json",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, ReservationDTO>>() {});
        return response.getBody().values(); // TODO try to better approach
    }
}