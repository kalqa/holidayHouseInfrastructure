package com.house.holiday.domain.boundary;

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
    public RoomResponseDTO getAvailableRooms(@QueryParam("arrivalDate") String arrivalDate,
                                             @QueryParam("leaveDate") String leaveDate) {
        return holidayHouseService.getAvailableRooms(arrivalDate, leaveDate);
    }
}