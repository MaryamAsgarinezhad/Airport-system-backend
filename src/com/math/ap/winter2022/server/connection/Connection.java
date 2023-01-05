package com.math.ap.winter2022.server.connection;

import com.math.ap.winter2022.server.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *  wrapper around socket which can be used to send and receive string messages
 */
public class Connection {
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected Scanner scanner;
    protected PrintWriter printWriter;

    public Connection(InputStream inputStream, OutputStream outputStream){
        this.inputStream=inputStream;
        this.outputStream=outputStream;

        this.scanner=new Scanner(this.inputStream);
        this.printWriter=new PrintWriter(this.outputStream);
    }

    public void send(String message) {
        printWriter.println(message);
        printWriter.flush();
    }

    public String nextLine() {
        return scanner.nextLine();
    }

}
