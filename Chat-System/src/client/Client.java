package client;

import javax.net.SocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    // private SocketFactory socketFactory = SocketFactory.getDefault();
    private static Socket socket;
    private Thread listener = null;
    private static PrintWriter out;
    private static BufferedReader br;

    public Client(String hostname, String port) {
        try {
            socket = new Socket(hostname, Integer.parseInt(port));
            out = new PrintWriter(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            listener = new Thread(this);
            listener.start();
            ConnectionManager cm = new ConnectionManager(socket,out);
            cm.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
/*        Scanner  scanner = new Scanner(System.in);
        System.out.println("Geben Sie bitte den Hostnamen ein:");
        String hostname = scanner.next();
        System.out.println("Portnummer:");
        String port = scanner.next();*/
        Client c1 = new Client("localhost", "9001");

    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = br.readLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            listener = null;
            try {
                out.close();
                br.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


