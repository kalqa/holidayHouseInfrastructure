package com.holiday.house.api.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class ReservationDTO {

    private Integer roomNumber;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date toDate;

    public ReservationDTO() {
    }

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date getToDate() {
        return toDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "roomNumber=" + roomNumber +
                ", userName='" + userName + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
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
            reservationDTO.setFromDate(this.fromDate);
            reservationDTO.setUserName(this.userName);
            reservationDTO.setToDate(this.toDate);
            reservationDTO.setRoomNumber(this.roomNumber);
            return reservationDTO;
        }
    }
}