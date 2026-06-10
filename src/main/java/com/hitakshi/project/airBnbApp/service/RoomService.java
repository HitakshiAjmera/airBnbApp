package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId,RoomDto roomDto);
    List<RoomDto> getAllRoomsInHotel(Long hotelId);
    RoomDto getRoomById(Long id);
    void  deleteRoomById(Long id);
}
