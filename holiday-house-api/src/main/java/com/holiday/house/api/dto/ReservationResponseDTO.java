package com.holiday.house.api.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationResponseDTO {

    @JsonProperty("name")
    private String id;

    private Map<String, ReservationDTO> reservationDTOs;

    public static ReservationResponseDTOBuilder builder() {
        return new ReservationResponseDTOBuilder();
    }

    public String getId() {
        return id;
    }

    public Map<String, ReservationDTO> getReservationDTOs() {
        return reservationDTOs;
    }

    @Override
    public String toString() {
        return "ReservationResponseDTO{" +
                "id='" + id + '\'' +
                ", reservationDTOs=" + reservationDTOs +
                '}';
    }

    public static final class ReservationResponseDTOBuilder {

        private String id;
        private Map<String, ReservationDTO> reservationDTOs;

        private ReservationResponseDTOBuilder() {
        }

        public ReservationResponseDTOBuilder withId(String message) {
            this.id = message;
            return this;
        }

        public ReservationResponseDTOBuilder withReservations(Map<String, ReservationDTO> reservationDTOs) {
            this.reservationDTOs = reservationDTOs;
            return this;
        }

        public ReservationResponseDTO build() {
            ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
            reservationResponseDTO.id = this.id;
            reservationResponseDTO.reservationDTOs = this.reservationDTOs;
            return reservationResponseDTO;
        }
    }
}