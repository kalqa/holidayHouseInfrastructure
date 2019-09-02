package com.house.holiday.domain.control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.client.boundary.HolidayHouseClient;
import com.house.holiday.client.control.HolidayHouseClientImpl;
import com.house.holiday.domain.boundary.HolidayHouseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HolidayHouseServiceTest {

    @Spy
    @InjectMocks
    HolidayHouseService holidayHouseService = new HolidayHouseServiceImpl();

    @Spy
    @InjectMocks
    ReservationManager reservationManager = new ReservationManager();

    @Spy
    RoomManager roomManager = new RoomManager();

    @Mock
    HolidayHouseClient holidayHouseClient = new HolidayHouseClientImpl();

    @Before
    public void before() {
        when(holidayHouseClient.makeReservation(getReservations().get(0))).thenReturn(getReservationResponses().get(0));
//        when(holidayHouseClient.makeReservation(getReservations().get(1))).thenReturn(getReservationResponses().get(1));
//        when(holidayHouseClient.makeReservation(getReservations().get(2))).thenReturn(getReservationResponses().get(2));
//        when(holidayHouseClient.makeReservation(getReservations().get(3))).thenReturn(getReservationResponses().get(3));

        when(holidayHouseClient.getAllRooms()).thenReturn(getAllRooms());
        when(holidayHouseClient.getAllReservations()).thenReturn(getAllReservations());
    }

    private ReservationResponseDTO getAllReservations() {
        ReservationDTO reservation1 = ReservationDTO.builder()
                .withUserName("kalka")
                .withFromDate(LocalDate.of(2019, 8, 15))
                .withToDate(LocalDate.of(2019, 8, 20))
                .withRoomNumber(123)
                .build();

        return ReservationResponseDTO.builder()
                .withReservations(Collections.singletonMap("321", reservation1))
                .build();
    }

    private RoomResponseDTO getAllRooms() {
        return RoomResponseDTO.builder().withAvailableRooms(Collections.singletonList(RoomDTO.builder().withRoomNumber(123).build())).build();
    }

    private Collection<RoomDTO> getAllAvailableRooms() {
//        List<RoomDTO> roomDTOS = new ArrayList<>();
//        roomDTOS.add(RoomDTO.builder().withRoomNumber(123).build());
//        return roomDTOS;

        return Collections.singleton(RoomDTO.builder().withRoomNumber(123).build());
    }

    @Test
    public void shouldMakeCorrectReservation() {
        ReservationResponseDTO reservationResponseDTO = holidayHouseService.makeReservation(getReservations().get(0));

        assertEquals(getReservationResponses().get(0), reservationResponseDTO);
    }

    private List<ReservationDTO> getReservations() {
        List<ReservationDTO> reservationDTOS = new ArrayList<>();

        ReservationDTO correctReservation = ReservationDTO.builder()
                .withUserName("kalka")
                .withRoomNumber(123)
                .withFromDate(LocalDate.of(2019, 8, 12))
                .withToDate(LocalDate.of(2019, 8, 14))
                .build();

        ReservationDTO collidingReservation = ReservationDTO.builder()
                .withUserName("kalka")
                .withRoomNumber(543)
                .withFromDate(LocalDate.of(2019, 8, 12))
                .withToDate(LocalDate.of(2019, 8, 14))
                .build();

        ReservationDTO toDateBeforeFromReservation = ReservationDTO.builder()
                .withUserName("kalka")
                .withRoomNumber(543)
                .withFromDate(LocalDate.of(2019, 8, 12))
                .withToDate(LocalDate.of(2019, 8, 8))
                .build();

        ReservationDTO roomNumberDoesntExistReservation = ReservationDTO.builder()
                .withUserName("kalka")
                .withRoomNumber(111)
                .withFromDate(LocalDate.of(2019, 8, 12))
                .withToDate(LocalDate.of(2019, 8, 8))
                .build();

        reservationDTOS.add(correctReservation);
        reservationDTOS.add(collidingReservation);
        reservationDTOS.add(toDateBeforeFromReservation);
        reservationDTOS.add(roomNumberDoesntExistReservation);

        return reservationDTOS;
    }

    private List<ReservationResponseDTO> getReservationResponses() {
        List<ReservationResponseDTO> reservationResponseDTOS = new ArrayList<>();

        ReservationResponseDTO correctReservationResponse = ReservationResponseDTO.builder()
                .withId("3333")
                .withMessage("543")
                .withReservations(Collections.singletonMap("3333", getReservations().get(0)))
                .build();

        reservationResponseDTOS.add(correctReservationResponse);

        return reservationResponseDTOS;
    }
}