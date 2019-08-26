package com.holiday.house.api.dto;

public class RoomDTO {

    private Integer roomNumber;

    public static RoomDTOBuilder builder() {
        return new RoomDTOBuilder();
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public static class RoomDTOBuilder {

        private Integer roomNumber;

        private RoomDTOBuilder() {
        }

        public RoomDTOBuilder withRoomNumber(Integer roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public RoomDTO build() {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomNumber(roomNumber);
            return roomDTO;
        }
    }
}