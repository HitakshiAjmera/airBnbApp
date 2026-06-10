package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.dto.HotelDto;
import com.hitakshi.project.airBnbApp.entity.Hotel;
import com.hitakshi.project.airBnbApp.exception.ResourceNotFoundException;
import com.hitakshi.project.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating new hotel with name: {} " , hotelDto.getName());

        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("created new hotel with name: {} " , hotelDto.getName());
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {

        log.info("Getting hotel with id: {} " , id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        log.info("Got hotel with id: {} " , id);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(HotelDto hotelDto, Long id) {
        log.info("Updating hotel with id: {} " , id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        log.info("Updated hotel with id: {} " , id);

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        log.info("deleting hotel with id: {} " , id);
        boolean exist = hotelRepository.existsById(id);
        if(!exist) throw new ResourceNotFoundException("Hotel not found with id : " +id);
        hotelRepository.deleteById(id);
        log.info("Deleted hotel with id: {} " , id);
    }

    @Override
    public void activateHotel(Long id) {
        log.info("activating hotel with id: {} " , id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " +id));

        hotel.setActive(true);
        log.info("activated hotel with id: {} " , id);
    }
}
