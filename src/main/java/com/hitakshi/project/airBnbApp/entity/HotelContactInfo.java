package com.hitakshi.project.airBnbApp.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable //this will place inside hotel table only, we can use it in many entities
public class HotelContactInfo {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
