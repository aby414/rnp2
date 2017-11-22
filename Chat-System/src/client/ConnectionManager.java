package client;

import javax.net.SocketFactory;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionManager extends Thread {

    private Client client;
    private Socket socket;


    public ConnectionManager(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
             input = scanner.nextLine();
            if (!input.toUpperCase().equals("QUIT") && !isInterrupted()) {
                client.getOut().println(input);
                client.getOut().flush();
            } else {
                break;
            }
        }

        scanner.close();

        client.getOut().close();
        try {
            client.getBr().close();
            client.getListener().interrupt();
            socket.close();
            System.out.println("closed manager");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
