package com.hitakshi.project.airBnbApp.controller;

import com.hitakshi.project.airBnbApp.dto.HotelDto;
import com.hitakshi.project.airBnbApp.dto.HotelInfoDto;
import com.hitakshi.project.airBnbApp.dto.HotelSearchRequest;
import com.hitakshi.project.airBnbApp.service.HotelService;
import com.hitakshi.project.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelDto> page =inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotel_id}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotel_id){

        return ResponseEntity.ok(hotelService.getHotelInfoById(hotel_id));

    }
}
