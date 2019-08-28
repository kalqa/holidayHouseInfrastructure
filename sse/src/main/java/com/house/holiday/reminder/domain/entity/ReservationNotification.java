package com.house.holiday.reminder.domain.entity;

public class ReservationNotification {

    private final String message;
    private final String userName;

    public ReservationNotification(String message, String userName) {
        this.message = message;
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }
}