import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class ClientMock {

    private static String nickName;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    //    private static Client restClient = ClientBuilder.newClient();
    private static Client restClient = Client.create();

    public static void main(String[] args) {

        System.out.println("Please give me your nickname: ");

        try {
            nickName = in.readLine();
        } catch (IOException e) {
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

        ClientResponse response = restClient
                .resource("http://127.0.0.1:8080/holiday-house-service/room")
                .queryParam("arrivalDate", arrivalDate)
                .queryParam("leaveDate", leaveDate)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        JSONObject roomResponse = response.getEntity(JSONObject.class);
        try {
            System.out.println(roomResponse.getJSONArray("availableRooms"));
            //maybe sleep
        } catch (JSONException e) {
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
}