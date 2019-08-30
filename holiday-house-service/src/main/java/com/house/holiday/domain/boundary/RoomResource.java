package com.house.holiday.domain.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.holiday.house.api.dto.RoomResponseDTO;
import com.house.holiday.domain.control.HolidayHouseServiceImpl;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate clientFromDate;
        LocalDate clientToDate;

        clientFromDate = LocalDate.parse(arrivalDate, formatter);
        clientToDate = LocalDate.parse(leaveDate, formatter);

        RoomResponseDTO roomResponse = holidayHouseService.getAvailableRooms(clientFromDate, clientToDate);
        return buildResponse(roomResponse);
    }

    private Response buildResponse(RoomResponseDTO roomResponse) {
        return Response.ok(roomResponse)
                .type(MediaType.APPLICATION_JSON).build();
    }
}