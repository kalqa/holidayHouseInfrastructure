package com.holiday.house.api.dto;

import java.util.Date;

public class ReservationNotificationDTO {

    private String userName;
    private Date when;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}