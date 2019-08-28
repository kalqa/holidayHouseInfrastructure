package com.holiday.house.api.dto;

import java.time.LocalDate;
import java.util.Date;

public class ReservationNotificationDTO {

    private String userName;
    private LocalDate when;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getWhen() {
        return when;
    }

    public void setWhen(LocalDate when) {
        this.when = when;
    }
}