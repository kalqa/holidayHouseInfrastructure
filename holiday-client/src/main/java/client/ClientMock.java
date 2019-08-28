package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

import com.holiday.house.api.dto.ReservationDTO;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class ClientMock {

    private static String nickName;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static Client restClient = Client.create();

    public static void main(String[] args) {

        System.out.println("Please give me your nickname: ");

        try {
            nickName = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventHandler eventHandler = new SimpleEventHandler();
        String url = String.format("http://localhost:8080/notification?nickName=" + nickName);
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));

        try (EventSource eventSource = builder.build()) {
            eventSource.setReconnectionTimeMs(3000);
            eventSource.start();

            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = getOption();

        while (i != 4) {
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
                    break;
                case 4:
                    return;
                default:
            }
        }
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
            ClientResponse response = restClient
                    .resource("http://127.0.0.1:8080/holiday-house-service/room")
                    .queryParam("arrivalDate", arrivalDate)
                    .queryParam("leaveDate", leaveDate)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            JSONObject roomResponse = response.getEntity(JSONObject.class);

            System.out.println(roomResponse.getJSONArray("availableRooms"));
            //maybe sleep
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

//        String arrivalDate = null;
//        String leaveDate = null;
        String roomNumber = null;
        try {
//            arrivalDate = in.readLine();
//            leaveDate = in.readLine();
            roomNumber = in.readLine();
            String arrivalDate = "10-08-2019";
            String leaveDate = "12-08-2019";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date arrivalDateParsed = simpleDateFormat.parse(arrivalDate);
            Date leaveDateParsed = simpleDateFormat.parse(leaveDate);

            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .withRoomNumber(Integer.parseInt(roomNumber))
                    .withFromDate(arrivalDateParsed)
                    .withToDate(leaveDateParsed)
                    .withUserName(nickName)
                    .build();

            ClientResponse response = restClient
                    .resource("http://127.0.0.1:8080/holiday-house-service/reservation")
                    .accept("application/json")
                    .type("application/json")
                    .post(ClientResponse.class, reservationDTO);

            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roomNumber == null) {
            return;
        }
    }
}