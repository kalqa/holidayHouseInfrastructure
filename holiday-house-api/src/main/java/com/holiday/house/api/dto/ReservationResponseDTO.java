//package com.holiday.house.api.dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class ReservationResponseDTO {
//
//    @JsonProperty("name")
//    private String id;
//
//    private ReservationDTO reservationDTO;
//
//    public String getId() {
//        return id;
//    }
//
//    public ReservationDTO getReservationDTO() {
//        return reservationDTO;
//    }
//
//    public static final class ReservationResponseDTOBuilder {
//
//        private String id;
//        private ReservationDTO reservationDTO;
//
//        private ReservationResponseDTOBuilder() {
//        }
//
//        public static ReservationResponseDTOBuilder aReservationResponseDTO() {
//            return new ReservationResponseDTOBuilder();
//        }
//
//        public ReservationResponseDTOBuilder withMessage(String message) {
//            this.id = message;
//            return this;
//        }
//
//        public ReservationResponseDTO build() {
//            ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
//            reservationResponseDTO.id = this.id;
//            return reservationResponseDTO;
//        }
//    }
//}