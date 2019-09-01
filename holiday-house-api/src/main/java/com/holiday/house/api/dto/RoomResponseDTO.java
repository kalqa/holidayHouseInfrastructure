package com.holiday.house.api.dto;

import java.util.Collection;

public class RoomResponseDTO {

    private Collection<RoomDTO> availableRooms;
    private String message;

    public static RoomResponseDTOBuilder builder() {
        return new RoomResponseDTOBuilder();
    }

    public Collection<RoomDTO> getAvailableRooms() {
        return availableRooms;
    }

    private void setAvailableRooms(Collection<RoomDTO> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RoomResponseDTO{" +
                "availableRooms=" + availableRooms +
                ", message='" + message + '\'' +
                '}';
    }

    public static final class RoomResponseDTOBuilder {

        private Collection<RoomDTO> availableRooms;
        private String message;

        private RoomResponseDTOBuilder() {
        }

        public RoomResponseDTOBuilder withAvailableRooms(Collection<RoomDTO> rooms) {
            this.availableRooms = rooms;
            return this;
        }

        public RoomResponseDTOBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public RoomResponseDTO build() {
            RoomResponseDTO roomResponse = new RoomResponseDTO();
            roomResponse.setAvailableRooms(this.availableRooms);
            roomResponse.setMessage(this.message);
            return roomResponse;
        }
    }
}