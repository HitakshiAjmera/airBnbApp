package com.hitakshi.project.airBnbApp.service;

import com.hitakshi.project.airBnbApp.entity.Room;

public interface InventoryService {

    void initializeRoomForYear(Room roomId);
    void deleteAllInventoriesOfRoom(Room room);
}
