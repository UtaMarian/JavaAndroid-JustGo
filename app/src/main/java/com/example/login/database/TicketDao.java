package com.example.login.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.login.data.Ticket;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insert(Ticket ticket);

    @Update
    void update(Ticket ticket);

    @Delete
    void delete(Ticket ticket);

    @Query("DELETE FROM tickets")
    void deleteAll();


    @Query("SELECT * FROM tickets WHERE id = :id")
    Ticket getById(int id);

    @Query("SELECT * FROM tickets ORDER BY id ASC")
    List<Ticket> getAll();
}
