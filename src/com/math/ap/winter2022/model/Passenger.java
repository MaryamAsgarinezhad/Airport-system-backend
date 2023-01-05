package com.math.ap.winter2022.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Passenger extends User {
    private HashMap<Integer,Integer> flights=new HashMap<>();
    public Passenger(String name, String password) {
        super(name, password);
    }

    public void setFlights(HashMap<Integer, Integer> flights) {
        this.flights = flights;
    }

    public HashMap<Integer, Integer> getFlights() {
        return flights;
    }
}
