package com.hitakshi.project.airBnbApp.repository;

import com.hitakshi.project.airBnbApp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository <Hotel,Long>{
}
