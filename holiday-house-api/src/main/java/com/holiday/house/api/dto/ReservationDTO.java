package com.holiday.house.api.dto;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class ReservationDTO {

    @JsonProperty("name")
    private String id;

    private Integer roomNumber;

    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate toDate;

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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        private String id;
        private Integer roomNumber;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String userName;

        private ReservationDTOBuilder() {
        }

        public ReservationDTOBuilder withRoomNumber(Integer roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public ReservationDTOBuilder withFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public ReservationDTOBuilder withToDate(LocalDate toDate) {
            this.toDate = toDate;
            return this;
        }

        public ReservationDTOBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public ReservationDTOBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public ReservationDTO build() {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setFromDate(this.fromDate);
            reservationDTO.setUserName(this.userName);
            reservationDTO.setToDate(this.toDate);
            reservationDTO.setRoomNumber(this.roomNumber);
            reservationDTO.setId(this.id);
            return reservationDTO;
        }
    }
}