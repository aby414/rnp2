package clientComponent;

import java.io.*;
import java.net.Socket;

public class MessageReader implements Runnable {

    private static Socket socket;
    private Thread listener = null;
    private PrintWriter out;
    private BufferedReader br;
    private MessageWriter cm;

    public MessageReader(String hostname, String port) {
        try {
            socket = new Socket(hostname, Integer.parseInt(port));
            out = new PrintWriter(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            listener = new Thread(this);
            listener.start();
            cm = new MessageWriter(this);
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
        MessageReader c1 = new MessageReader("localhost", "9001");

    }

    @Override
    public void run() {
        while (!listener.isInterrupted()) {
            try {
                while (true) {
                    String line = br.readLine();
                    System.out.println(line);
                }
            } catch (IOException e) {
                out.close();
                System.out.println("Connection lost to host.");
                listener.interrupt();
                if (cm.isInterrupted() == false) {
                    cm.interrupt();
                }
                System.out.println("closed clientComponent");
            }
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getBr() {
        return br;
    }

    public Thread getListener() {
        return listener;
    }

    public static Socket getSocket() {
        return socket;
    }
}


