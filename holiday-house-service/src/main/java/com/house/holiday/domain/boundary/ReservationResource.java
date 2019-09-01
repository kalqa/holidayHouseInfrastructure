package com.house.holiday.domain.boundary;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;

@Produces(MediaType.APPLICATION_JSON)
@Path(ReservationResource.RESOURCE_PATH)
public class ReservationResource {

    public static final String RESOURCE_PATH = "reservation";

    @Inject
    HolidayHouseService holidayHouseService;

    @GET
    public ReservationResponseDTO getAllReservations(@QueryParam("nickName") String nickName) {
        return holidayHouseService.getAllReservationsByNickName(nickName);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public ReservationResponseDTO makeReservation(ReservationDTO reservationDTO) {
        return holidayHouseService.makeReservation(reservationDTO);
    }

    @DELETE
    @Path("/{id}")
    public ReservationResponseDTO cancelReservation(@PathParam("id") String id) {
        return holidayHouseService.cancelReservationById(id);
    }
}