package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.dto.RoomDto;
import com.hitakshi.project.airBnbApp.entity.Hotel;
import com.hitakshi.project.airBnbApp.entity.Room;
import com.hitakshi.project.airBnbApp.entity.User;
import com.hitakshi.project.airBnbApp.exception.ResourceNotFoundException;
import com.hitakshi.project.airBnbApp.exception.UnAuthorisedException;
import com.hitakshi.project.airBnbApp.repository.HotelRepository;
import com.hitakshi.project.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;


    @Override
    public RoomDto createNewRoom(Long hotelId,RoomDto roomDto) {
        log.info("creating a new room in hotel with id: {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }
        Room room = modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if(hotel.getActive()){
            inventoryService.initializeRoomForYear(room);
        }

        log.info("Created a new room in hotel with id: {}",hotelId);

        return modelMapper.map(room,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("creating a new room in hotel with id: {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        return hotel.getRooms()
                .stream()
                .map((element)->
                        modelMapper.map(element,RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long id) {

        log.info("Getting room by id {}",id);
        Room room = roomRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id "));

        return modelMapper.map(room,RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long id) {

        log.info("Deleting Room by id {}",id);
        /*Boolean isExits = roomRepository.existsById(id);
        if(!isExits) throw new ResourceNotFoundException("Room not found with id :"+id);*/
        Room room = roomRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id "));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())) {
            throw new UnAuthorisedException("This user does not own this room with id: "+id);
        }

        inventoryService.deleteAllInventoriesOfRoom(room);
        roomRepository.deleteById(id);
    }
}
