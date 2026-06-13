package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.dto.HotelDto;
import com.hitakshi.project.airBnbApp.dto.HotelInfoDto;
import com.hitakshi.project.airBnbApp.dto.RoomDto;
import com.hitakshi.project.airBnbApp.entity.Hotel;
import com.hitakshi.project.airBnbApp.entity.Room;
import com.hitakshi.project.airBnbApp.entity.User;
import com.hitakshi.project.airBnbApp.exception.ResourceNotFoundException;
import com.hitakshi.project.airBnbApp.exception.UnAuthorisedException;
import com.hitakshi.project.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomService roomService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating new hotel with name: {} " , hotelDto.getName());

        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("created new hotel with name: {} " , hotelDto.getName());
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {

        log.info("Getting hotel with id: {} " , id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        log.info("Got hotel with id: {} " , id);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(HotelDto hotelDto, Long id) {
        log.info("Updating hotel with id: {} " , id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        log.info("Updated hotel with id: {} " , id);

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("deleting hotel with id: {} " , id);
        /*boolean exist = hotelRepository.existsById(id);
        if(!exist) throw new ResourceNotFoundException("Hotel not found with id : " +id);*/

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        for(Room room : hotel.getRooms()) {
            inventoryService.deleteAllInventoriesOfRoom(room);
            roomService.deleteRoomById(room.getId());

        }
        hotelRepository.deleteById(id);
        log.info("Deleted hotel with id: {} " , id);
    }

    @Override
    @Transactional
    public void activateHotel(Long id) {
        log.info("activating hotel with id: {} " , id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        hotel.setActive(true);
        //assuming will do it once
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForYear(room);
        }

        log.info("activated hotel with id: {} " , id);
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        log.info("get hotel info with id: {} " ,hotelId );
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +hotelId));
        List<RoomDto> rooms =  hotel.getRooms()
                .stream()
                .map((elements)-> modelMapper.map(elements,RoomDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),rooms);
    }
}
