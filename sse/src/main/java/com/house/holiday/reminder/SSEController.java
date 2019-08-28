package com.house.holiday.reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.house.holiday.reminder.entity.ReservationNotification;

@Controller
public class SSEController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

//    @GetMapping("/createNotification")
//    public ReservationNotificationResponseDTO registerNotification(ReservationNotificationDTO reservationNotificationDTO){
//        String userName = reservationNotificationDTO.getUserName();
//        Date when = reservationNotificationDTO.getWhen();
//
//        // utworz dla dlanego userName'a notyfikacje
//
//    }

    // tutaj dorobic queryParam dla userName i if'a zeby
    @GetMapping("/notification")
    public SseEmitter handle(HttpServletResponse response, @RequestParam String nickName) {
        SseEmitter emitter = new SseEmitter();

        if ("kalka".equals(nickName)) {
            // get notifications from database for kalka
            response.setHeader("Cache-Control", "no-store");

            this.emitters.add(emitter);

            emitter.onCompletion(() -> this.emitters.remove(emitter));
            emitter.onTimeout(() -> this.emitters.remove(emitter));

            return emitter;
        }

        return emitter;
    }

    @EventListener
    public void onNotificationSend(ReservationNotification reservationNotification) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(reservationNotification);
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }
}
