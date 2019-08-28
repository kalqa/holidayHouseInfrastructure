package com.house.holiday.domain.boundary;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.house.holiday.domain.control.HolidayHouseServiceImpl;
import com.house.holiday.domain.entity.RoomResponse;

@Produces(MediaType.APPLICATION_JSON)
@Path(RoomResource.RESOURCE_PATH)
public class RoomResource {

    public static final String RESOURCE_PATH = "room";

    @Inject
    HolidayHouseServiceImpl holidayHouseService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableRooms(@QueryParam("arrivalDate") String arrivalDate,
                                      @QueryParam("leaveDate") String leaveDate) {

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        LocalDate clientFromDate;
        LocalDate clientToDate;

        clientFromDate = LocalDate.parse(arrivalDate);
        clientToDate = LocalDate.parse(leaveDate);

        RoomResponse roomResponse = holidayHouseService.getAvailableRooms(clientFromDate, clientToDate);
        return buildResponse(roomResponse);
    }

//    private Response buildFailResponse(String message) {
//        return Response.status(Status.BAD_REQUEST)
//                .entity(RoomResponse.builder()
//                        .withMessage(message)
//                        .build())
//                .type(MediaType.APPLICATION_JSON).build();
//    }

    private Response buildResponse(RoomResponse roomResponse) {
        return Response.ok(roomResponse)
                .type(MediaType.APPLICATION_JSON).build();
    }
}