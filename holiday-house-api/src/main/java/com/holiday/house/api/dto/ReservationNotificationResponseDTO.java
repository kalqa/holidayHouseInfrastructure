package com.holiday.house.api.dto;

public class ReservationNotificationResponseDTO {

    private String message;
    private boolean created;
    private ReservationNotificationDTO reservationNotificationDTO;

    public ReservationNotificationResponseDTO() {
    }

    public ReservationNotificationDTO getReservationNotificationDTO() {
        return reservationNotificationDTO;
    }

    public void setReservationNotificationDTO(ReservationNotificationDTO reservationNotificationDTO) {
        this.reservationNotificationDTO = reservationNotificationDTO;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}