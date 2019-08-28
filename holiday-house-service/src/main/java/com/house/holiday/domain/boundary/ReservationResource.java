package com.house.holiday.domain.boundary;

import java.util.Collection;

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

@Produces(MediaType.APPLICATION_JSON)
@Path(ReservationResource.RESOURCE_PATH)
public class ReservationResource {

    public static final String RESOURCE_PATH = "reservation";

    @Inject
    HolidayHouseService holidayHouseService;

    @Inject
    ReservationMapper reservationMapper;

    @GET
    public Collection<ReservationDTO> getAllReservations(@QueryParam("nickName") String nickName) {
        return holidayHouseService.getAllReservationsByNickName(nickName);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public ReservationDTO makeReservation(ReservationDTO reservationDTO) {
        return holidayHouseService.makeReservation(reservationDTO);

    }

    @DELETE
    @Path("/{id}")
    public ReservationDTO cancelReservation(@PathParam("id") String id) {
        return holidayHouseService.cancelReservationById(id);
    }
}