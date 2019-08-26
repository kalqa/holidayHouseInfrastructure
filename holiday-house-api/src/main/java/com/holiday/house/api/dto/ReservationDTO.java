package com.holiday.house.api.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationDTO {

    private Integer roomNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date toDate;
    private String userName;

    public static ReservationDTOBuilder builder() {
        return new ReservationDTOBuilder();
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static final class ReservationDTOBuilder {

        private Integer roomNumber;
        private Date fromDate;
        private Date toDate;
        private String userName;

        private ReservationDTOBuilder() {
        }

        public ReservationDTOBuilder withRoomNumber(Integer roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public ReservationDTOBuilder withFromDate(Date fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public ReservationDTOBuilder withToDate(Date toDate) {
            this.toDate = toDate;
            return this;
        }

        public ReservationDTOBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public ReservationDTO build() {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.fromDate = this.fromDate;
            reservationDTO.userName = this.userName;
            reservationDTO.toDate = this.toDate;
            reservationDTO.roomNumber = this.roomNumber;
            return reservationDTO;
        }
    }
}