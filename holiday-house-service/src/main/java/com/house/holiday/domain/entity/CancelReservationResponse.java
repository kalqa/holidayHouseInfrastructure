package com.house.holiday.domain.entity;

public class CancelReservationResponse {

    private String message;
    private String id;

    public static CancelReservationResponseBuilder builder() {
        return new CancelReservationResponseBuilder();
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public static final class CancelReservationResponseBuilder {

        private String message;
        private String id;

        private CancelReservationResponseBuilder() {
        }

        public CancelReservationResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public CancelReservationResponseBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public CancelReservationResponse build() {
            CancelReservationResponse cancelReservationResponse = new CancelReservationResponse();
            cancelReservationResponse.message = this.message;
            cancelReservationResponse.id = this.id;
            return cancelReservationResponse;
        }
    }
}