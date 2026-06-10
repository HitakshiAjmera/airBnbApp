package com.hitakshi.project.airBnbApp.dto;

import com.hitakshi.project.airBnbApp.entity.Hotel;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {

    private Long id;
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;

}
