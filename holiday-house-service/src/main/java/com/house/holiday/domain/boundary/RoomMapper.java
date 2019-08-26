package com.house.holiday.domain.boundary;

import com.holiday.house.api.dto.RoomDTO;
import com.house.holiday.domain.entity.Room;

public class RoomMapper {

    public Room mapToRoom(RoomDTO roomDto) {
        Room room = new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        return room;
    }
}