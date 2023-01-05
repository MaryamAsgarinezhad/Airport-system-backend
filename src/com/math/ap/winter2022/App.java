package com.math.ap.winter2022;

import com.math.ap.winter2022.server.Controller;
import com.math.ap.winter2022.server.Server;
import com.math.ap.winter2022.server.connection.ServerConnection;


import java.util.Scanner;

public class App {



    public static void main(String[] args) {
        Controller controller = new Controller();

        Server server = new Server(controller);

        ServerConnection connection = server.connectToServer();



        // TODO : add flights using Controller.insertFlight here to test your code



        new Thread(() -> {
            while (true) {
                String response = connection.nextLine();

                System.out.println(response);
            }
        }).start();
        // start notificationsThread to see the notifications in the error stream
        Thread notificationsThread = new Thread(() -> {
            while (true) {
                System.err.println(connection.nextNotification());
            }
        });

        Scanner scanner = new Scanner(System.in);
        while (true){
            connection.send(scanner.nextLine());
        }


    }

}