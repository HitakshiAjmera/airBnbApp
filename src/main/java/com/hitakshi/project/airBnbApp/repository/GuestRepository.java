package com.hitakshi.project.airBnbApp.repository;

import com.hitakshi.project.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
