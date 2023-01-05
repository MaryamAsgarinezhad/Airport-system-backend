package test;

import com.math.ap.winter2022.server.connection.StreamConnector;

import java.io.PrintWriter;
import java.util.Scanner;

public class mainTest {

    public static void main(String[] args){

        StreamConnector streamConnector=new StreamConnector();
        StreamConnector.PipeConnection pipeConnection1=streamConnector.getPipedConnection();
        StreamConnector.PipeConnection pipeConnection2=streamConnector.getPipedConnection();

        PrintWriter printWriter1=new PrintWriter(pipeConnection1.outputStream,true);

        Scanner scanner1=new Scanner(pipeConnection1.inputStream);

        new Thread(() -> {
            while (true){
                printWriter1.println("salam");
                printWriter1.flush();
            }
        }).start();

        new Thread(() -> {
            while (true){
                String s=scanner1.nextLine();
                System.out.println(s);
            }
        }).start();


    }
}
