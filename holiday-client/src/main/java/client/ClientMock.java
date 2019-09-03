package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class ClientMock {

    private static String nickName;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static Client restClient;
    private static Thread notificationThread;

    public static void main(String[] args) {
        disableLogsFromSseProtcol();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JacksonJsonProvider provider = new JacksonJsonProvider(mapper);
        restClient = ClientBuilder.newClient(new ClientConfig().register(provider));

        System.out.println("Please give me your nickname: ");

        try {
            nickName = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startNotificationListenerInNewThread();

        int i;
        do {
            i = getOption();
            switch (i) {
                case 1:
                    handleAvailableRoomsSelection();
                    break;
                case 2:
                    handleMakingReservation();
                    break;
                case 3:
                    handleCancelingReservation(nickName);
                    break;
                default:
                    notificationThread.interrupt();
                    return;
            }
        } while (true);
    }

    private static void handleCancelingReservation(String nickName) {

        try {
            Response response = restClient
                    .target("http://127.0.0.1:8080/holiday-house-service/reservation")
                    .queryParam("nickName", nickName)
                    .request()
                    .get(Response.class);

            ReservationResponseDTO reservationResponseDTO = response.readEntity(new GenericType<ReservationResponseDTO>() {});

            Map<String, ReservationDTO> reservationDTOs = reservationResponseDTO.getReservationDTOs();

            List<String> ids = new ArrayList<>();
            int i = 0;
            for (Map.Entry entry : reservationDTOs.entrySet()) {
                ids.add(entry.getKey().toString());
                ReservationDTO reservationDTO = (ReservationDTO) entry.getValue();
                System.out.println("Reservation " + (i + 1) + ":" + entry.getKey() + ", Room: " + reservationDTO.getRoomNumber() + " from: " + reservationDTO.getFromDate().toString() + " to: " + reservationDTO.getToDate().toString());
                i++;
            }

            System.out.println("Please select one of your reservation. Just type in number associated with reservation");
            System.out.println("Type 'q' and hit enter to leave");
            String reservationClientInput;
            try {
                reservationClientInput = in.readLine();
                if (reservationClientInput.equalsIgnoreCase("q")) {
                    return;
                }
                int reservationNumber = Integer.parseInt(reservationClientInput);

                String s = ids.get(reservationNumber - 1);
                Response deleteResponse = requestReservationDeleteById(s);

                if (deleteResponse.getStatus() == Status.OK.getStatusCode()) {
                    ReservationResponseDTO reservationResponseDTO1 = deleteResponse.readEntity(ReservationResponseDTO.class);
                    System.out.println("Your reservation with id: " + reservationResponseDTO1.getId() + " has been canceled");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Response requestReservationDeleteById(String i1) {
        return restClient
                .target("http://127.0.0.1:8080/holiday-house-service/reservation")
                .path(i1)
                .request()
                .delete();
    }

    private static void startNotificationListenerInNewThread() {
        NotificationListener notificationListener = new NotificationListener(nickName);
        notificationThread = new Thread(notificationListener);
        notificationThread.start();
    }

    private static void disableLogsFromSseProtcol() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.OFF);
    }

    private static int getOption() {
        showMenu();
        String option;
        int i = 100;
        do {
            try {
                option = in.readLine();
                i = Integer.parseInt(option);
                if (!(i >= 1 && i <= 4)) {
                    System.out.println("sorry please give number from 1-3");
                }
            } catch (Exception e) {
                System.out.println("sorry please give number");
            }
        } while (!(i >= 1 && i <= 4));
        return i;
    }

    private static void showMenu() {
        System.out.println("1. List all rooms");
        System.out.println("2. Make reservation for given room number and date");
        System.out.println("3. Cancel one of your reservations");
        System.out.println("4. Exit");
    }

    private static void handleAvailableRoomsSelection() {
        System.out.println("Please give me arrival date (dd-MM-yyyy)");
        System.out.println("Please give me date when you will leave room (dd-MM-yyyy)");

        String arrivalDate = null;
        String leaveDate = null;
        try {
            arrivalDate = in.readLine();
            leaveDate = in.readLine();
            if (!isDateInCorrectFormat(arrivalDate, leaveDate)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Response response = restClient
                    .target("http://127.0.0.1:8080/holiday-house-service/room")
                    .queryParam("arrivalDate", arrivalDate)
                    .queryParam("leaveDate", leaveDate)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class);

            if (response.getStatus() == Status.OK.getStatusCode()) {
                RoomResponseDTO roomResponse = response.readEntity(new GenericType<RoomResponseDTO>() {});
                System.out.println("Available rooms: ");
                roomResponse.getAvailableRooms().forEach(roomDTO -> System.out.println(roomDTO.getRoomNumber().toString()));
            } else {
                System.out.println("Sorry problem with server. Response from server: " + response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isDateInCorrectFormat(String arrivalDate, String leaveDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.parse(arrivalDate);
            simpleDateFormat.parse(leaveDate);
        } catch (ParseException e) {
            System.out.println("sorry error while parsing data");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void handleMakingReservation() {
        System.out.println("Please give me arrival date (dd-MM-yyyy)");
        System.out.println("Please give me date when you will leave room (dd-MM-yyyy)");
        System.out.println("Please give me room number");

        String arrivalDate;
        String leaveDate;
        String roomNumber;
        try {
            arrivalDate = in.readLine();
            leaveDate = in.readLine();
            roomNumber = in.readLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate arrivalDateParsed = LocalDate.parse(arrivalDate, formatter);
            LocalDate leaveDateParsed = LocalDate.parse(leaveDate, formatter);

            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .withRoomNumber(Integer.parseInt(roomNumber))
                    .withFromDate(arrivalDateParsed)
                    .withToDate(leaveDateParsed)
                    .withUserName(nickName)
                    .build();

            Response response = restClient
                    .target("http://127.0.0.1:8080/holiday-house-service/reservation")
                    .request()
                    .post(Entity.entity(reservationDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Status.OK.getStatusCode()) {
                ReservationResponseDTO reservationResponse = response.readEntity(ReservationResponseDTO.class);
                System.out.println("Created reservation with id: " + reservationResponse.getId());
            } else {
                System.out.println("Did not create reservation. Response status: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}