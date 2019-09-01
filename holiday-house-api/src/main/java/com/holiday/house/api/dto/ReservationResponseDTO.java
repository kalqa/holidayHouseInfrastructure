package com.holiday.house.api.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationResponseDTO {

    @JsonProperty("name")
    private String id;

    private String message;

    private Map<String, ReservationDTO> reservationDTOs;

    public static ReservationResponseDTOBuilder builder() {
        return new ReservationResponseDTOBuilder();
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, ReservationDTO> getReservationDTOs() {
        return reservationDTOs;
    }

    private void setReservationDTOs(Map<String, ReservationDTO> reservationDTOs) {
        this.reservationDTOs = reservationDTOs;
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
        private String message;

        private ReservationResponseDTOBuilder() {
        }

        public ReservationResponseDTOBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public ReservationResponseDTOBuilder withReservations(Map<String, ReservationDTO> reservationDTOs) {
            this.reservationDTOs = reservationDTOs;
            return this;
        }

        public ReservationResponseDTOBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ReservationResponseDTO build() {
            ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
            reservationResponseDTO.setId(this.id);
            reservationResponseDTO.setReservationDTOs(this.reservationDTOs);
            reservationResponseDTO.setMessage(this.message);
            return reservationResponseDTO;
        }
    }
}