package com.math.ap.winter2022.server;

import com.math.ap.winter2022.model.Flight;
import com.math.ap.winter2022.model.Passenger;
import com.math.ap.winter2022.model.User;
import com.math.ap.winter2022.server.connection.ServerConnection;
import com.math.ap.winter2022.server.connection.StreamConnector;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private User user;
    private InputStream inputStream;
    private OutputStream outputStream;
    private OutputStream notificationOutStream;
    private Controller controller;
    private Scanner scanner;
    private PrintWriter printWriter;
    public Server(Controller controller) {
        this.controller=controller;
    }

    public ServerConnection connectToServer(){
        StreamConnector streamConnector=new StreamConnector();
        StreamConnector.PipeConnection pipeConnection1=streamConnector.getPipedConnection();
        StreamConnector.PipeConnection pipeConnection2=streamConnector.getPipedConnection();
        StreamConnector.PipeConnection pipeConnection3=streamConnector.getPipedConnection();

        ServerConnection serverConnection=new ServerConnection(pipeConnection1.inputStream,pipeConnection2.outputStream,pipeConnection3.inputStream);
        this.inputStream=pipeConnection2.inputStream;
        this.outputStream=pipeConnection1.outputStream;
        this.notificationOutStream=pipeConnection3.outputStream;
        this.scanner=new Scanner(pipeConnection2.inputStream);
        this.printWriter=new PrintWriter(pipeConnection1.outputStream);
        handle();
        return serverConnection;
    }
    public void handle(){
        new Thread(() -> {
            while (true){
                handleReq(scanner.nextLine());
            }
        }).start();
    }
    public void send(String message){
        printWriter.println(message);
        printWriter.println(">>");
        printWriter.flush();
    }
    private void handleReq(String rep) {
        String[] s=rep.split(" ");

        if(s[0].equals("register")){
            try {
                String username=s[1].substring(1,s[1].length()-1);
                String pass=s[2].substring(1,s[2].length()-1);

                controller.register("passenger",username,pass);
            } catch (Exception e) {
                send("error!");
            }
        }
        else{
            if(s[0].equals("login")){
                try {
                    String username=s[1].substring(1,s[1].length()-1);
                    String pass=s[2].substring(1,s[2].length()-1);

                    User user1=controller.login("passenger",username,pass);
                    this.user=user1;
                } catch (Exception e) {
                    send("error!");
                }
            }
            else{
                if(s[0].equals("search")){
                    List<Flight> flights1=new ArrayList<>();
                    List<Flight> flights2=new ArrayList<>();
                    List<Flight> flights3=new ArrayList<>();
                    List<Flight> flights4=new ArrayList<>();
                    List<Flight> flights5=new ArrayList<>();


                    int i=0;
                    for (String temp:s){
                        i++;
                        if(i==1){
                            continue;
                        }
                        else{
                            List<String> property=findProperty(temp);
                            String check=property.get(1);

                            switch (property.get(0)) {

                                case "departure" -> {
                                    for (Flight f:controller.allFlights){
                                        if(f.getDate1().equals(check)){
                                            flights1.add(f);
                                        }
                                    }
                                }

                                case "airline" -> {
                                    for (Flight f:flights1){
                                        if(f.getAirline1().equals(check)){
                                            flights2.add(f);
                                        }
                                    }
                                }

                                case "from" -> {
                                    for (Flight f:flights2){
                                        if(f.getFrom1().equals(check)){
                                            flights3.add(f);
                                        }
                                    }
                                }

                                case "to" -> {
                                    for (Flight f:flights3){
                                        if(f.getTo1().equals(check)){
                                            flights4.add(f);
                                        }
                                    }
                                }

                                case "price_range" -> {
                                    for (Flight f:flights4){
                                        String[] ss=check.split("-");
                                        if(f.getTicketPrice1()>Integer.valueOf(ss[0]) && f.getTicketPrice1()<Integer.valueOf(ss[1])){
                                            flights5.add(f);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ShowBySort(flights5);
                }
                else{
                    if(s[0].equals("reserve")){
                        try {
                            int flightNo= Integer.parseInt(s[1].substring(1,s[1].length()-1));
                            int n= Integer.parseInt(s[2].substring(1,s[2].length()-1));

                            controller.purchase(this.user,flightNo,n);
                        } catch (Exception e) {
                            send("error!");
                        }
                    }
                    else{
                        if(s[0].equals("reservations")){
                            List<Flight> flights=new ArrayList<>();
                            Passenger passenger=(Passenger)this.user;
                            for (int flightNo:passenger.getFlights().keySet()){
                                Flight flight=findFlightFromNumber(flightNo);
                                flights.add(flight);
                            }

                            ShowBySort2(flights);
                        }
                    }
                }
            }
        }
    }
    private void ShowBySort2(List<Flight> flights5) {
        List<Flight> flights=new ArrayList<>(flights5);
        List<Flight> ans=new ArrayList<>();

        for (int i = 0; i < flights5.size(); i++) {
            Flight temp=flights.get(0);
            for (Flight flight:flights){
                if(flight.getDate1().compareTo(temp.getDate1())<0){
                    temp=flight;
                }
                else{
                    if(flight.getDate1().compareTo(temp.getDate1())==0){
                        if(flight.getTime1().compareTo(temp.getTime1())<0){
                            temp=flight;
                        }
                        else{
                            if(flight.getTime1().compareTo(temp.getTime1())==0){
                                if(flight.getTicketPrice1()< temp.getTicketPrice1()){
                                    temp=flight;
                                }
                            }
                        }
                    }
                }
            }

            ans.add(temp);
            flights.remove(temp);
        }

        String ansStr="";

        int i=0;
        for (Flight flight:ans){
            i++;
            ansStr+=flight.FlighttoString2();

            //
            int num=((Passenger)this.user).getFlights().get(flight.getFlightNumber1());
            ansStr+=num;

            if(i!=ans.size()){
                ansStr+=String.format("%n");
            }
        }

        send(ansStr);
    }
    private Flight findFlightFromNumber(int number){
        for (Flight flight:controller.allFlights){
            if(flight.getFlightNumber1()==number){
                return flight;
            }
        }
        return null;
    }
    private void ShowBySort(List<Flight> flights5) {

        List<Flight> flights=new ArrayList<>(flights5);
        List<Flight> ans=new ArrayList<>();

        for (int i = 0; i < flights5.size(); i++) {
            Flight temp=flights.get(0);
            for (Flight flight:flights){
                if(flight.getDate1().compareTo(temp.getDate1())<0){
                    temp=flight;
                }
                else{
                    if(flight.getDate1().compareTo(temp.getDate1())==0){
                        if(flight.getTime1().compareTo(temp.getTime1())<0){
                            temp=flight;
                        }
                        else{
                            if(flight.getTime1().compareTo(temp.getTime1())==0){
                                if(flight.getTicketPrice1()< temp.getTicketPrice1()){
                                    temp=flight;
                                }
                            }
                        }
                    }
                }
            }

            ans.add(temp);
            flights.remove(temp);
        }

        String ansStr="";

        int i=0;
        for (Flight flight:ans){
            i++;
            ansStr+=flight.FlighttoString();

            if(i!=ans.size()){
                ansStr+=String.format("%n");
            }
        }

        send(ansStr);
    }
    public List<String> findProperty(String s){
        String[] ss=s.split(":");

        List<String > ans=new ArrayList<>();

        ans.add(ss[0].substring(1));
        ans.add(ss[1].substring(0,ss[1].length()-1));

        return ans;
    }

}
