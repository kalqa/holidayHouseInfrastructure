package com.house.holiday.domain.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.house.holiday.client.boundary.HolidayHouseClient;
import com.house.holiday.client.control.HolidayHouseClientImpl;
import com.house.holiday.domain.boundary.HolidayHouseService;

public class HolidayHouseServiceImplTest {

    @Spy
    @InjectMocks
    ReservationManager reservationManager = new ReservationManager();

    @Spy
    @InjectMocks
    HolidayHouseService holidayHouseService = new HolidayHouseServiceImpl();

    @Spy
    @InjectMocks
    HolidayHouseClient holidayHouseClient = new HolidayHouseClientImpl();


    @Before
    public void before() {

    }

    @Test
    public void shouldMakeCorrectReservation() {
        ReservationResponseDTO shouldReturn = ReservationResponseDTO.builder()
                .withId("123123")
                .withMessage("created nicely")
                .build();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ReservationDTO reservationDto = ReservationDTO.builder()
                .withFromDate(LocalDate.parse("08-08-2019", formatter))
                .withToDate(LocalDate.parse("12-08-2019",formatter))
                .withUserName("kalka")
                .build();

        Assert.assertEquals(shouldReturn, holidayHouseService.makeReservation(reservationDto));
    }
}