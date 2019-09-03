package com.house.holiday.reminder.domain.control;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.holiday.house.api.dto.ReservationDTO;
import com.house.holiday.reminder.client.boundary.ReservationClient;
import com.house.holiday.reminder.domain.entity.ReservationNotification;

@Service
public class NotificationJob {

    private static final String REMEMBER_ABOUT_YOUR_RESERVATION_TOMORROW = "Remember about your reservation tomorrow!";

    private final ApplicationEventPublisher eventPublisher;
    private final ReservationClient reservationClient;

    @Autowired
    public NotificationJob(ApplicationEventPublisher eventPublisher, ReservationClient reservationClient) {
        this.eventPublisher = eventPublisher;
        this.reservationClient = reservationClient;
    }

    //    @Scheduled(fixedRate = 3600000)
    @Scheduled(fixedRate = 1000)
    public void doSomething() {
        Collection<ReservationDTO> allReservationsFromFireBase = reservationClient.getAllReservations();

        LocalDate currentDay = LocalDate.now();
        List<ReservationNotification> createdNotifications = allReservationsFromFireBase.stream()
                .filter(reservationDTO -> isOneDayBeforeReservation(currentDay, reservationDTO))
                .map(this::getReservationNotification)
                .collect(Collectors.toList());

        createdNotifications.forEach(this.eventPublisher::publishEvent);
    }

    private ReservationNotification getReservationNotification(ReservationDTO reservationDTO) {
        return new ReservationNotification(REMEMBER_ABOUT_YOUR_RESERVATION_TOMORROW, reservationDTO.getUserName());
    }

    private boolean isOneDayBeforeReservation(LocalDate currentDay, ReservationDTO reservationDTO) {
        return reservationDTO.getFromDate().minusDays(1).isEqual(currentDay);
    }
}