package com.house.holiday.domain.entity;

import java.util.List;

public class RoomResponse {

    private List<Room> availableRooms;

    public static RoomResponseBuilder builder() {
        return new RoomResponseBuilder();
    }

    public List<Room> getAvailableRooms() {
        return availableRooms;
    }

    public static final class RoomResponseBuilder {

        private List<Room> availableRooms;

        private RoomResponseBuilder() {
        }

        public RoomResponseBuilder withAvailableRooms(List<Room> rooms) {
            this.availableRooms = rooms;
            return this;
        }

        public RoomResponse build() {
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.availableRooms = this.availableRooms;
            return roomResponse;
        }
    }
}
