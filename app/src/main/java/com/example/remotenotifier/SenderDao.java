package com.example.remotenotifier;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SenderDao {

    @Query("SELECT * FROM senderDB")
    List<SenderEntity> getAll();

    @Query("SELECT * FROM senderDB WHERE sid  LIKE :id")
    SenderEntity loadBySid(String id);

    @Insert
    void insert(SenderEntity sender);

    @Delete
    void delete(SenderEntity sender);
}
