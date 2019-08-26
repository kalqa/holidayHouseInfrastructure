package com.house.holiday.domain.boundary;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.holiday.house.api.dto.CancelReservationResponseDTO;
import com.holiday.house.api.dto.ReservationDTO;
import com.holiday.house.api.dto.ReservationResponseDTO;
import com.house.holiday.domain.control.HolidayHouseServiceImpl;
import com.house.holiday.domain.entity.CancelReservationResponse;
import com.house.holiday.domain.entity.ReservationResponse;

@Produces(MediaType.APPLICATION_JSON)
@Path(ReservationResource.RESOURCE_PATH)
public class ReservationResource {

    public static final String RESOURCE_PATH = "reservation";

    @Inject
    HolidayHouseServiceImpl holidayHouseService;

    @Inject
    ReservationMapper reservationMapper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public ReservationResponseDTO makeReservation(ReservationDTO reservationDTO) {
        ReservationResponse reservationResponse = holidayHouseService.makeReservation(reservationDTO);
        return ReservationResponseDTO.ReservationResponseDTOBuilder
                .aReservationResponseDTO()
                .withMessage(reservationResponse.getReservationId())
                .build();
    }

    @DELETE
    @Path("/{id}")
    public CancelReservationResponseDTO cancelReservation(@PathParam("id") String id) {
        CancelReservationResponse cancelReservationResponse = holidayHouseService.cancelReservationById(id);
        return CancelReservationResponseDTO.CancelReservationResponseDTOBuilder
                .aCancelReservationResponseDTO()
                .withMessage(cancelReservationResponse.getMessage())
                .build();
    }
}