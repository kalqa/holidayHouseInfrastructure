package com.house.holiday.reminder;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.house.holiday.reminder.entity.ReservationNotification;

@Service
public class NotificationJob {

    public final ApplicationEventPublisher eventPublisher;

    public NotificationJob(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 10000)
    public void doSomething() {

        // raz dziennie sprawdzic w bazie danych czy jest jakis wpis do wyslania dla danego uzytkownika?


        ReservationNotification reservationNotification = new ReservationNotification("powiadomienia do kalki!");
        this.eventPublisher.publishEvent(reservationNotification);
    }
}