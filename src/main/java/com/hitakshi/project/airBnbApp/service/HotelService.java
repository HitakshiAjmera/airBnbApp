package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.dto.HotelDto;
import com.hitakshi.project.airBnbApp.entity.Hotel;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(HotelDto hotelDto,Long id);
    void deleteHotelById(Long id);
    void activateHotel(Long id);
}
