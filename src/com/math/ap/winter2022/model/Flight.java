package com.math.ap.winter2022.model;


import java.util.ArrayList;
import java.util.List;

public class Flight{
    public List<Flight> allFlights=new ArrayList<>();
    private String airline;
    private int flightNumber;
    private String from;
    private String to;
    private String date;
    private String time;
    private long ticketPrice;
    private int capacity;

    // feature suggestion: flight builder/factory class
    public Flight(
        String airline, int flightNumber,
        String from, String to, 
        String date, String time, 
        long ticketPrice, int capacity) {

        this.airline=airline;
        this.flightNumber=flightNumber;
        this.from=from;
        this.ticketPrice=ticketPrice;
        this.time=time;
        this.to=to;
        this.date=date;
        this.capacity=capacity;

        allFlights.add(this);
    }

    public List<Flight> getAllFlights() {
        return allFlights;
    }

    public String getAirline1() {
        return airline;
    }

    public int getFlightNumber1() {
        return flightNumber;
    }

    public String getFrom1() {
        return from;
    }

    public String getTo1() {
        return to;
    }

    public String getDate1() {
        return date;
    }

    public String getTime1() {
        return time;
    }

    public long getTicketPrice1() {
        return ticketPrice;
    }

    public int getCapacity1() {
        return capacity;
    }

    public boolean hasCapacity(){
        if(this.capacity>allFlights.size()){
            return true;
        }
        else{
            return false;
        }
    }

    public String FlighttoString(){
        String num;
        if(hasCapacity()){
            num=String.valueOf(this.capacity-allFlights.size());
        }
        else{
            num="full";
        }
        return "airline:"+this.airline+" "+
                "flight_no:"+this.flightNumber+" "+
                "from:"+this.from+" "+
                "to:"+this.to+" "+
                "departure_date:"+this.date+" "+
                "departure_time:"+this.time+" "+
                "price:"+this.getTicketPrice()+" "+
                "available_seats:"+num;

    }

    public String FlighttoString2(){
        String num;
        if(hasCapacity()){
            num=String.valueOf(this.capacity-allFlights.size());
        }
        else{
            num="full";
        }
        return "airline:"+this.airline+" "+
                "flight_no:"+this.flightNumber+" "+
                "from:"+this.from+" "+
                "to:"+this.to+" "+
                "departure_date:"+this.date+" "+
                "departure_time:"+this.time+" "+
                "price:"+this.getTicketPrice()+" "+
                "number:";

    }


    public int getFlightNumber() {
        return -1;
    }



    public long getPrice() {
        return -1;
    }



    public String getDate() {
        return null;
    }

    public String getTime() {
        return null;
    }

    public int getCapacity() {
        return -1;
    }



    public String getFrom() {
        return null;
    }

    public String getTo() {
        return null;
    }

    public long getTicketPrice() {
        return -1;
    }


    public String getAirline() {
        return null;
    }
}
