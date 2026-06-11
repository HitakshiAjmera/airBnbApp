package com.hitakshi.project.airBnbApp.repository;

import com.hitakshi.project.airBnbApp.entity.Inventory;
import com.hitakshi.project.airBnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    void deleteByRoom(Room room);
}
