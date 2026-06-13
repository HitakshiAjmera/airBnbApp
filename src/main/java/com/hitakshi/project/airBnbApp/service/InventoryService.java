package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.dto.HotelDto;
import com.hitakshi.project.airBnbApp.dto.HotelPriceDto;
import com.hitakshi.project.airBnbApp.dto.HotelSearchRequest;
import com.hitakshi.project.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForYear(Room roomId);
    void deleteAllInventoriesOfRoom(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
