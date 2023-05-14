package com.example.login.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.login.data.Ticket;

@Database(entities = {Ticket.class}, version = 1)
public abstract class TicketDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();
}
