package server;

import java.io.*;
import java.net.Socket;

public class Client extends Thread {

    private Room room;
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public  void run(){
        try {
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            //Send hello message to Client
            while(true){
                out.writeBytes("Hallo Fremder. Soll ich dir die Liste aller Räume anzeigen?");
                String answer = in.readLine();
                if(answer.toUpperCase().equals("JA")){
                    out.writeBytes(Server.getRooms().toString());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
