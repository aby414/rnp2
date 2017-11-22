package clientComponent;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MessageWriter extends Thread {

    private MessageReader messageReader;
    private Socket socket;


    public MessageWriter(MessageReader messageReader) {
        this.socket = messageReader.getSocket();
        this.messageReader = messageReader;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.nextLine();
            if (!input.toUpperCase().equals("QUIT") && !isInterrupted()) {
                messageReader.getOut().println(input);
                messageReader.getOut().flush();
            } else {
                break;
            }
        }

        scanner.close();

        messageReader.getOut().close();
        try {
            messageReader.getBr().close();
            messageReader.getListener().interrupt();
            socket.close();
            System.out.println("closed manager");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
