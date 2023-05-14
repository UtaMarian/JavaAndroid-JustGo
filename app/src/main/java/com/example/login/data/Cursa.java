package com.example.login.data;

import java.io.Serializable;
import java.time.LocalDate;

public class Cursa implements Serializable {
    private Integer id;
    private Integer busId;
    private String departure;
    private String arrival;
    private String departureDate;
    private String arrivalDate;
    private float price;
    private String bus_Plate_number;
    private String bus_Type;
    private  int capacity;
//    private String company;
//    private Integer nrLocuri;


    @Override
    public String toString() {
        return "Cursa{" +
                "id=" + id +
                ", busId=" + busId +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", price=" + price +
                ", bus_Plate_number='" + bus_Plate_number + '\'' +
                ", bus_Type='" + bus_Type + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    public Cursa(){}

    public Cursa(Integer id, Integer busId, String departure, String arrival, String departureDate, String arrivalDate, float price, String bus_Plate_number, String bus_Type, int capacity) {
        this.id = id;
        this.busId = busId;
        this.departure = departure;
        this.arrival = arrival;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.price = price;
        this.bus_Plate_number = bus_Plate_number;
        this.bus_Type = bus_Type;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBus_Plate_number() {
        return bus_Plate_number;
    }

    public void setBus_Plate_number(String bus_Plate_number) {
        this.bus_Plate_number = bus_Plate_number;
    }

    public String getBus_Type() {
        return bus_Type;
    }

    public void setBus_Type(String bus_Type) {
        this.bus_Type = bus_Type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


}
