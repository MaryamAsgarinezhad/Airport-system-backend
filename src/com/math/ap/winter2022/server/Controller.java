package com.math.ap.winter2022.server;

import com.math.ap.winter2022.model.Airline;
import com.math.ap.winter2022.model.Flight;
import com.math.ap.winter2022.model.Passenger;
import com.math.ap.winter2022.model.User;

import java.util.*;
import java.util.regex.Pattern;


public class Controller {
    public List<Flight> allFlights=new ArrayList<>();
    public List<String> allUsernames=new ArrayList<>();
    public List<User> allUsers=new ArrayList<>();
    public Controller() {

    }

    public void register(String userType, String username, String password) throws Exception {
        if(!userType.equals("passenger") && !userType.equals("airline")){
            throw new Exception();
        }
        else{
            if(this.allUsernames.contains(username)){
                throw new Exception();
            }
            else{
                this.allUsernames.add(username);
                if(userType.equals("passenger")){
                    this.allUsers.add(new Passenger(username,password));
                }
                else{
                    this.allUsers.add(new Airline(username,password));
                }
            }
        }
    }

    public User login(String userType, String username, String password) throws Exception {
        if(!userType.equals("passenger") && !userType.equals("airline")){
            throw new Exception();
        }
        else{
            if(!this.allUsernames.contains(username)){
                throw new Exception();
            }
            else{
                User user=findUserFromUsername(username);
                if(user.getPassword()!=password){
                    throw new Exception();
                }
                else{
                    return findUserFromUsername(username);
                }
            }
        }
    }

    public void insertFlight(User user, String args) throws Exception {
        String[] splitted=args.split(" ");
        if(findFlightFromNumber(Integer.parseInt(splitted[0]))!=null){
            exceptionTake();
            return;
        }
        if(!(user instanceof Airline)){
            exceptionTake();
            return;
        }

        if(splitted.length!=7 || !isNumeric(splitted[0]) || !isNumeric(splitted[5]) || !isNumeric(splitted[6])){
            exceptionTake();
            return;
        }

        if(!timeCheck(splitted[4])){
            exceptionTake();
            return;
        }

        if(!dateCheck(splitted[3])){
            exceptionTake();
            return;
        }

        this.allFlights.add(new Flight(user.getUsername(),Integer.parseInt(splitted[0]),splitted[2],splitted[1],splitted[3],splitted[4],Long.valueOf(splitted[5]),Integer.parseInt(splitted[6])));
    }
    private User findUserFromUsername(String username){
        for (User user:this.allUsers){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
    private Flight findFlightFromNumber(int number){
        for (Flight flight:this.allFlights){
            if(flight.getFlightNumber1()==number){
                return flight;
            }
        }
        return null;
    }

    public void purchase(User user, int flightNo, int n) throws Exception {
        String UserType=findTypeOfUser(user);
        if(UserType.equals("airline")){
            throw new Exception("permission denied");
        }
        else{
            if(findFlightFromNumber(flightNo).getCapacity1()<n){
                throw new Exception("not enough seats available");
            }
            else{
                Passenger passenger=(Passenger)user;

                if(passenger.getFlights().keySet().contains(flightNo)){
                    passenger.getFlights().replace(flightNo,passenger.getFlights().get(flightNo)+n);
                }
            }
        }
    }

    private String findTypeOfUser(User user) {
        if(user instanceof Airline){
            return "airline";
        }
        else{
            return "passenger";
        }
    }

    public List<String> reservations(User user) {
        return null;
    }
    public void exceptionTake() throws Exception {
        throw new Exception();
    }

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    private boolean timeCheck(String time){
        String[] splitted=time.split(":");
        if(splitted.length!=2){
            return false;
        }
        else{
            if(!isNumeric(splitted[0]) || !isNumeric(splitted[1])){
                return false;
            }
            else{
                if(Integer.valueOf(splitted[0])>23 || Integer.valueOf(splitted[0])<0 || Integer.valueOf(splitted[1])>59 || Integer.valueOf(splitted[1])<0){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }

    private boolean dateCheck(String time){
        String[] splitted=time.split("-");
        if(splitted.length!=3){
            return false;
        }
        else{
            if(!isNumeric(splitted[0]) || !isNumeric(splitted[1])  || !isNumeric(splitted[2])){
                return false;
            }
            else{
                if(Integer.valueOf(splitted[0])>30 || Integer.valueOf(splitted[0])<0 || Integer.valueOf(splitted[1])>12 || Integer.valueOf(splitted[1])<0){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }

}
