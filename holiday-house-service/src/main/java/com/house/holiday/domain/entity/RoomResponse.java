package com.house.holiday.domain.entity;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RoomResponse {

    private List<Room> availableRooms;
    private Optional<String> message;

    public static RoomResponseBuilder builder() {
        return new RoomResponseBuilder();
    }

    public List<Room> getAvailableRooms() {
        return availableRooms;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public static final class RoomResponseBuilder {

        private List<Room> availableRooms;
        private Optional<String> message;

        private RoomResponseBuilder() {
        }

        public RoomResponseBuilder withAvailableRooms(List<Room> rooms) {
            this.availableRooms = rooms;
            return this;
        }

        public RoomResponseBuilder withMessage(String message) {
            this.message = Optional.of(message);
            return this;
        }

        public RoomResponse build() {
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.availableRooms = this.availableRooms;
            roomResponse.message = this.message;
            return roomResponse;
        }
    }
}
