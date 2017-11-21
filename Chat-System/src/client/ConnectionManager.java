package client;

import javax.net.SocketFactory;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionManager extends Thread {

    private Socket socket;
    private static DataOutputStream out;


    public ConnectionManager(Socket socket, DataOutputStream out) {
        this.socket = socket;
        this.out = out;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                out.writeBytes(scanner.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
