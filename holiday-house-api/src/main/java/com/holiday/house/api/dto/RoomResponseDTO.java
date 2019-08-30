package com.holiday.house.api.dto;

import java.util.Collection;

public class RoomResponseDTO {

    private Collection<RoomDTO> availableRooms;

    public static RoomResponseDTOBuilder builder() {
        return new RoomResponseDTOBuilder();
    }

    public Collection<RoomDTO> getAvailableRooms() {
        return availableRooms;
    }

    @Override
    public String toString() {
        return "RoomResponseDTO{" +
                "availableRooms=" + availableRooms +
                '}';
    }

    public static final class RoomResponseDTOBuilder {

        private Collection<RoomDTO> availableRooms;

        private RoomResponseDTOBuilder() {
        }

        public RoomResponseDTOBuilder withAvailableRooms(Collection<RoomDTO> rooms) {
            this.availableRooms = rooms;
            return this;
        }

        public RoomResponseDTO build() {
            RoomResponseDTO roomResponse = new RoomResponseDTO();
            roomResponse.availableRooms = this.availableRooms;
            return roomResponse;
        }
    }
}