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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.holiday.house.api.dto.RoomResponseDTO;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.LoggerFactory;

public class ClientMock {

    private static String nickName;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    //    private static Client restClient = Client.create();
    private static Client restClient;

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

        int i = 0;
        while (i != 4) {
            i = getOption();
            switch (i) {
                case 1:
                    handleAvailableRoomsSelection();
                    i = getOption();
                    break;
                case 2:
                    handleMakingReservation();
                    i = getOption();
                    break;
                case 3:
                    handleCancelingReservation(nickName);
                    i = getOption();
                    break;
                case 4:
                    return;
                default:
            }
        }
    }

    private static void handleCancelingReservation(String nickName) {
        // 1. get All nickname reservation
        // 2. list the ids for to the UI
        // 3. get number

        List<String> ids = new ArrayList<>();

        try {
            Response response = restClient
                    .target("http://127.0.0.1:8080/holiday-house-service/reservation")
                    .queryParam("nickName", nickName)
                    .request()
                    .get(Response.class);

            ReservationResponseDTO reservationResponseDTO = response.readEntity(new GenericType<ReservationResponseDTO>() {});

            Map<String, ReservationDTO> reservationDTOs = reservationResponseDTO.getReservationDTOs();

            int i = 0;
            for (Map.Entry entry : reservationDTOs.entrySet()) {
                ids.add(entry.getKey().toString());
                System.out.println("Reservation " + (i + 1) + ":" + entry.getKey() + ", " + ((ReservationDTO) entry.getValue()).getRoomNumber());
                i++;
            }

            System.out.println("Please select one of your reservation. Just type in number associated with reservation");
            String reservationNo;
            try {
                reservationNo = in.readLine();
                int i1 = Integer.parseInt(reservationNo);

                String s = ids.get(i1 - 1);
                Response deleteResponse = requestReservationDeleteById(s);

                if (deleteResponse.getStatus() == Status.OK.getStatusCode()) {
                    ReservationResponseDTO reservationResponseDTO1 = deleteResponse.readEntity(ReservationResponseDTO.class);
                    System.out.println(reservationResponseDTO1.getId());
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
        Thread t1 = new Thread(notificationListener);
        t1.start();
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
//            String arrivalDate = "10-08-2019";
//            String leaveDate = "12-08-2019";
            if (!isDateInCorrectFormat(arrivalDate, leaveDate)) {
                //logger data is not correct
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

            RoomResponseDTO roomResponse = response.readEntity(new GenericType<RoomResponseDTO>() {});

            System.out.println(roomResponse.getAvailableRooms());

            System.out.println("Available rooms: ");

            roomResponse.getAvailableRooms().forEach(roomDTO -> System.out.println(roomDTO.getRoomNumber().toString()));
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
//        System.out.println("Please give me arrival date (dd-MM-yyyy)");
//        System.out.println("Please give me date when you will leave room (dd-MM-yyyy)");
        System.out.println("Please give me room number");

//        String arrivalDate = null;
//        String leaveDate = null;
        String roomNumber = null;
        try {
//            arrivalDate = in.readLine();
//            leaveDate = in.readLine();
            roomNumber = in.readLine();
            String arrivalDate = "10-08-2019";
            String leaveDate = "12-08-2019";

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
                ReservationDTO reservationDTO1 = response.readEntity(ReservationDTO.class);
                System.out.println("Reservation id: " + reservationDTO1.getId());
            } else {
                // zaimplementowac reservation response dto
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roomNumber == null) {
            return;
        }
    }
}