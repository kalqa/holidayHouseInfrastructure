package com.holiday.house.api.dto;

import java.util.List;

public class RoomResponseDTO {

    private List<RoomDTO> availableRooms;

    public static RoomResponseDTOBuilder builder() {
        return new RoomResponseDTOBuilder();
    }

    public List<RoomDTO> getAvailableRooms() {
        return availableRooms;
    }

    public static final class RoomResponseDTOBuilder {

        private List<RoomDTO> availableRooms;

        private RoomResponseDTOBuilder() {
        }

        public RoomResponseDTOBuilder withAvailableRooms(List<RoomDTO> rooms) {
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