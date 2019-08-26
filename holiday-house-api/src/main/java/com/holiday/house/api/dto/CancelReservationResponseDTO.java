package com.holiday.house.api.dto;

public class CancelReservationResponseDTO {

    private String message;

    public String getMessage() {
        return message;
    }

    public static final class CancelReservationResponseDTOBuilder {

        private String message;

        private CancelReservationResponseDTOBuilder() {
        }

        public static CancelReservationResponseDTOBuilder aCancelReservationResponseDTO() {
            return new CancelReservationResponseDTOBuilder();
        }

        public CancelReservationResponseDTOBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public CancelReservationResponseDTO build() {
            CancelReservationResponseDTO cancelReservationResponseDTO = new CancelReservationResponseDTO();
            cancelReservationResponseDTO.message = this.message;
            return cancelReservationResponseDTO;
        }
    }
}