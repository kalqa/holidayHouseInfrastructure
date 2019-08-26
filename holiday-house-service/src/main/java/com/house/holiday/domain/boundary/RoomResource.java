package com.house.holiday.domain.boundary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.house.holiday.domain.control.HolidayHouseServiceImpl;
import com.house.holiday.domain.entity.RoomResponse;

@Produces("application/json")
@Path(RoomResource.RESOURCE_PATH)
public class RoomResource {

    public static final String RESOURCE_PATH = "room";

    @Inject
    HolidayHouseServiceImpl holidayHouseService;

    @GET
    public Response getAvailableRooms() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date clientFromDate = null;
        Date clientToDate = null;
        try {
            clientFromDate = sdf.parse("09-08-2019");
            clientToDate = sdf.parse("10-08-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RoomResponse roomResponse = holidayHouseService.getAvailableRooms(clientFromDate, clientToDate);
        return responseMapper(roomResponse);
    }

    private Response responseMapper(RoomResponse roomResponse) {
//        return Response.status(roomResponse.getStatus())
//                .entity(roomResponse.getEntity())
//                .type(roomResponse.getType())
//                .build();

        return Response.ok(roomResponse.getAvailableRooms()).type(MediaType.APPLICATION_JSON).build();
    }
}