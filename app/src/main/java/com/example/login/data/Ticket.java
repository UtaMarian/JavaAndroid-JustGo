package com.example.login.data;


import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = "tickets")
public class Ticket {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private int routeId;
    private int busId;
    private String userId;
    private String dateSchedule;
    private String ticketType;
    private int price;
    private int seatNumber;

    public Ticket(){}
    public Ticket(int id, int routeId, int busId, String userId, String dateSchedule, String ticketType, int price, int seatNumber) {
        this.id = id;
        this.routeId = routeId;
        this.busId = busId;
        this.userId = userId;
        this.dateSchedule = dateSchedule;
        this.ticketType = ticketType;
        this.price = price;
        this.seatNumber = seatNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateSchedule() {
        return dateSchedule;
    }

    public void setDateSchedule(String dateSchedule) {
        this.dateSchedule = dateSchedule;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
