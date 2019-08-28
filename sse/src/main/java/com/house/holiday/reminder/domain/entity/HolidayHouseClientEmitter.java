package com.house.holiday.reminder.domain.entity;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class HolidayHouseClientEmitter extends SseEmitter {

    private String nickName;

    public HolidayHouseClientEmitter(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}