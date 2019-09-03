package com.house.holiday.domain.control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.client.boundary.HolidayHouseClient;
import com.house.holiday.client.control.HolidayHouseClientImpl;
import com.house.holiday.domain.boundary.HolidayHouseService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HolidayHouseServiceTest {

    @Mock
    HolidayHouseClient holidayHouseClient = new HolidayHouseClientImpl();

    @Spy
    @InjectMocks
    HolidayHouseService holidayHouseService = new HolidayHouseServiceImpl();

    @Spy
    @InjectMocks
    ReservationManager reservationManager = new ReservationManager();

    @Spy
    @InjectMocks
    RoomManager roomManager = new RoomManager();

    @Before
    public void before() {
        when(holidayHouseClient.getAllRooms()).thenReturn(getAllRooms());
        when(holidayHouseClient.getAllReservations()).thenReturn(getAllReservations());
    }

    private RoomResponseDTO getAllRooms() {
        List<RoomDTO> roomDTOS = new ArrayList<>();

        RoomDTO room1 = RoomDTO.builder().withRoomNumber(123).build();

        roomDTOS.add(room1);

        return RoomResponseDTO.builder()
                .withAvailableRooms(roomDTOS)
                .build();
    }

    @Test
    public void shouldReturnCorrectMadeReservationResponse() {
        ReservationDTO correctReservation = getReservations().get(0);
        ReservationResponseDTO correctReservationResponse = getReservationResponses().get(0);
        when(holidayHouseClient.makeReservation(correctReservation)).thenReturn(correctReservationResponse);

        ReservationResponseDTO correctReservationResponseFromService = holidayHouseService.makeReservation(correctReservation);

        assertEquals(correctReservationResponse.getMessage(), correctReservationResponseFromService.getMessage());
    }

    @Test
    public void shouldReturnLeaveDateBeforeArrivalDateReservationResponse() {
        ReservationDTO leaveDateBeforeArrivalDateReservation = getReservations().get(2);
        ReservationResponseDTO leaveDateBeforeArrivalDateReservationResponse = getReservationResponses().get(2);
        when(holidayHouseClient.makeReservation(leaveDateBeforeArrivalDateReservation)).thenReturn(leaveDateBeforeArrivalDateReservationResponse);

        ReservationResponseDTO leaveDateBeforeArrivalDateReservationResponseFromService = holidayHouseService.makeReservation(leaveDateBeforeArrivalDateReservation);

        assertEquals(leaveDateBeforeArrivalDateReservationResponse.getMessage(), leaveDateBeforeArrivalDateReservationResponseFromService.getMessage());
    }

    @Test
    public void shouldReturnCollidingReservationResponse() {
        ReservationDTO collidingReservation = getReservations().get(1);
        ReservationResponseDTO collidingReservationResponse = getReservationResponses().get(1);
        when(holidayHouseClient.makeReservation(collidingReservation)).thenReturn(collidingReservationResponse);

        ReservationResponseDTO collidingReservationResponseFromService = holidayHouseService.makeReservation(collidingReservation);
        assertEquals(collidingReservationResponse.getMessage(), collidingReservationResponseFromService.getMessage());
    }

    @Test
    public void shouldReturnRoomDoesntExistReservationResponse() {
        ReservationDTO roomNumberDoesntExistReservation = getReservations().get(3);
        ReservationResponseDTO roomNumberDoesntExistReservationResponse = getReservationResponses().get(3);
        when(holidayHouseClient.makeReservation(roomNumberDoesntExistReservation)).thenReturn(roomNumberDoesntExistReservationResponse);

        ReservationResponseDTO roomNumberDoesntExistReservationResponseFromService = holidayHouseService.makeReservation(roomNumberDoesntExistReservation);

        assertEquals(roomNumberDoesntExistReservationResponse.getMessage(), roomNumberDoesntExistReservationResponseFromService.getMessage());
    }

    @Test
    public void shouldReturnCorrectCancelReservationResponse() {
        ReservationResponseDTO correctCancelReservationResponse = getReservationResponses().get(4);
        when(holidayHouseClient.cancelReservationById("5656")).thenReturn(correctCancelReservationResponse);

        ReservationResponseDTO correctCancelReservationResponseFromService = holidayHouseService.cancelReservationById("5656");

        assertEquals(correctCancelReservationResponse.getId(), correctCancelReservationResponseFromService.getId());
    }

    @Test
    public void shouldReturnCouldNotCancelReservationResponse() {
        ReservationResponseDTO couldNotCancelReservationResponse = getReservationResponses().get(5);
        when(holidayHouseClient.cancelReservationById("666666")).thenReturn(couldNotCancelReservationResponse);

        ReservationResponseDTO couldNotCancelReservationResponseFromService = holidayHouseService.cancelReservationById("666666");

        assertEquals(couldNotCancelReservationResponse.getMessage(), couldNotCancelReservationResponseFromService.getMessage());
    }

    @Test
    public void shouldReturnParseDateErrorRoomResponse() {
        RoomResponseDTO parseDateErrorRoomResponse = getRoomResponses().get(0);
        when(holidayHouseClient.getAllRooms()).thenReturn(parseDateErrorRoomResponse);

        RoomResponseDTO parseDateErrorRoomResponseFromService = holidayHouseService.getAvailableRooms("10--2019", "12-02-2019");

        assertEquals(parseDateErrorRoomResponse.getMessage(), parseDateErrorRoomResponseFromService.getMessage());
    }

    @Test
    public void shouldReturnCorrectAvailableRoomResponse() {
        RoomResponseDTO correctRetrievingAvailableRoomsResponse = getRoomResponses().get(1);
        when(holidayHouseClient.getAllRooms()).thenReturn(correctRetrievingAvailableRoomsResponse);

        RoomResponseDTO correctRetrievingAvailableRoomsResponseFromService = holidayHouseService.getAvailableRooms("10-08-2019", "12-02-2019");

        List<RoomDTO> expectedAvailableRooms = (List<RoomDTO>) correctRetrievingAvailableRoomsResponse.getAvailableRooms();
        List<RoomDTO> availableRoomsFromService = (List<RoomDTO>) correctRetrievingAvailableRoomsResponseFromService.getAvailableRooms();

        assertEquals(expectedAvailableRooms.size(), availableRoomsFromService.size());
        assertEquals(expectedAvailableRooms.get(0).getRoomNumber(), availableRoomsFromService.get(1).getRoomNumber());
        assertEquals(expectedAvailableRooms.get(1).getRoomNumber(), availableRoomsFromService.get(0).getRoomNumber());
        assertEquals(expectedAvailableRooms.get(2).getRoomNumber(), availableRoomsFromService.get(2).getRoomNumber());
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
                .withRoomNumber(123)
                .withFromDate(LocalDate.of(2019, 8, 17))
                .withToDate(LocalDate.of(2019, 8, 20))
                .build();

        ReservationDTO leaveDateBeforeArrivalDateReservation = ReservationDTO.builder()
                .withUserName("kalka")
                .withRoomNumber(123)
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
        reservationDTOS.add(leaveDateBeforeArrivalDateReservation);
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

        ReservationResponseDTO collidingReservationResponse = ReservationResponseDTO.builder()
                .withMessage("Room with number: 123 is busy from 2019-08-17 to 2019-08-20")
                .build();

        ReservationResponseDTO leaveDateBeforeArrivalDateReservationResponse = ReservationResponseDTO.builder()
                .withMessage("Leave date [2019-08-12] must be after arrival date [2019-08-08]!")
                .build();

        ReservationResponseDTO roomDoesntExistsReservationResponse = ReservationResponseDTO.builder()
                .withMessage("The room with number: [111] does not exists")
                .build();

        ReservationResponseDTO successCancelReservationResponse = ReservationResponseDTO.builder()
                .withId("5656")
                .build();

        ReservationResponseDTO couldNotCancelReservationResponse = ReservationResponseDTO.builder()
                .withMessage("Could not cancel reservation by given Id: 666666")
                .build();

        reservationResponseDTOS.add(correctReservationResponse);
        reservationResponseDTOS.add(collidingReservationResponse);
        reservationResponseDTOS.add(leaveDateBeforeArrivalDateReservationResponse);
        reservationResponseDTOS.add(roomDoesntExistsReservationResponse);
        reservationResponseDTOS.add(successCancelReservationResponse);
        reservationResponseDTOS.add(couldNotCancelReservationResponse);
        return reservationResponseDTOS;
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

    private List<RoomResponseDTO> getRoomResponses() {
        List<RoomResponseDTO> roomResponses = new ArrayList<>();

        RoomResponseDTO parseDateErrorRoomResponse = RoomResponseDTO.builder()
                .withMessage("Parse error for fromDate: 10--2019 and toDate: 12-02-2019")
                .withAvailableRooms(getMockedRooms())
                .build();

        RoomResponseDTO correctRetrievingAvailableRoomsResponse = RoomResponseDTO.builder()
                .withAvailableRooms(getMockedRooms())
                .build();

        roomResponses.add(parseDateErrorRoomResponse);
        roomResponses.add(correctRetrievingAvailableRoomsResponse);
        return roomResponses;
    }

    private List<RoomDTO> getMockedRooms() {
        List<RoomDTO> roomDTOS = new ArrayList<>();

        RoomDTO room1 = RoomDTO.builder().withRoomNumber(123).build();
        RoomDTO room2 = RoomDTO.builder().withRoomNumber(215).build();
        RoomDTO room3 = RoomDTO.builder().withRoomNumber(333).build();

        roomDTOS.add(room1);
        roomDTOS.add(room2);
        roomDTOS.add(room3);

        return roomDTOS;
    }
}