package com.house.holiday.reminder.domain.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.house.holiday.reminder.domain.entity.HolidayHouseClientEmitter;
import com.house.holiday.reminder.domain.entity.ReservationNotification;

@Controller
public class SSEController {

    private final CopyOnWriteArrayList<HolidayHouseClientEmitter> houseClientEmitters = new CopyOnWriteArrayList<>();

    @GetMapping("/notification")
    public SseEmitter handle(HttpServletResponse response, @RequestParam String nickName) {
        HolidayHouseClientEmitter emitter = new HolidayHouseClientEmitter(nickName);
        response.setHeader("Cache-Control", "no-store");

        this.houseClientEmitters.add(emitter);

        emitter.onCompletion(() -> this.houseClientEmitters.remove(emitter));
        emitter.onTimeout(() -> this.houseClientEmitters.remove(emitter));

        return emitter;
    }

    @EventListener
    public void onNotificationSend(ReservationNotification reservationNotification) {
        List<HolidayHouseClientEmitter> deadEmitters = new ArrayList<>();

//        SseEmitter.event()
//                .data(reservationNotification)
//                .name("mozna rozroznic ;)")
//                .id(String.valueOf(reservationNotification.hashCode()))
//                .build()

        this.houseClientEmitters.forEach(clientEmitter -> {
            try {
                if (isReservationForClient(reservationNotification, clientEmitter)) {
                    clientEmitter.send(reservationNotification);
                }
            } catch (Exception e) {
                deadEmitters.add(clientEmitter);
            }
        });

        this.houseClientEmitters.removeAll(deadEmitters);
    }

    private boolean isReservationForClient(ReservationNotification reservationNotification, HolidayHouseClientEmitter emitter) {
        return reservationNotification.getUserName().equals(emitter.getNickName());
    }
}