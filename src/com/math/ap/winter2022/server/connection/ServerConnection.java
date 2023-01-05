package com.math.ap.winter2022.server.connection;

import com.math.ap.winter2022.server.connection.Connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerConnection extends Connection {
    private InputStream notificationInputStream;

    public ServerConnection(InputStream inputStream, OutputStream outputStream, InputStream notifcationInputStream) {
        super(inputStream, outputStream);

        this.notificationInputStream=notifcationInputStream;
    }

    public String nextNotification() {
        Scanner scanner1=new Scanner(notificationInputStream);
        String ans=scanner1.nextLine();
        return ans;
    }
}
