package client;

import javax.net.SocketFactory;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionManager extends Thread {

    private Socket socket;
    private static PrintWriter out;


    public ConnectionManager(Socket socket, PrintWriter out) {
        this.socket = socket;
        this.out = out;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            out.println(input);
            out.flush();
        }

    }
}
