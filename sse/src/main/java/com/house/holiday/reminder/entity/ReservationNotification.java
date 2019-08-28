package com.house.holiday.reminder.entity;

public class ReservationNotification {

    private final String message;

    public ReservationNotification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}