package com.hitakshi.project.airBnbApp.dto;

import com.hitakshi.project.airBnbApp.entity.User;
import com.hitakshi.project.airBnbApp.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
