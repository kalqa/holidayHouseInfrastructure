package com.house.holiday.domain.entity;

public class ReservationResponse {

    private String message;
    private String reservationId;

    public static ReservationResponseBuilder builder() {
        return new ReservationResponseBuilder();
    }

    public String getMessage() {
        return message;
    }

    public String getReservationId() {
        return reservationId;
    }

    public static final class ReservationResponseBuilder {

        private String message;
        private String reservationId;

        private ReservationResponseBuilder() {
        }

        public ReservationResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ReservationResponseBuilder withReservationId(String reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public ReservationResponse build() {
            ReservationResponse reservationResponse = new ReservationResponse();
            reservationResponse.message = this.message;
            reservationResponse.reservationId = reservationId;
            return reservationResponse;
        }
    }
}
