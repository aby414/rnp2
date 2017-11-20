package server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client extends Thread {

    private Room room;
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            //Send hello message to Client
            while (true) {
                out.writeBytes("SELECTNAME \n");
                while (true) {
                    String name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (Server.getNames()) {
                        if (!Server.getNames().contains(name)) {
                            Server.getNames().add(name);
                            break;
                        } else {
                            out.writeBytes("INVALID");
                            out.writeBytes("SELECTNAME");
                        }
                    }
                }
                break;
            }

            out.writeBytes("ACCEPTED \n");
            Server.getOutputStreams().add(out);

            while (true) {
                String input = in.readLine();
                if (input.equals("ROOMS")) {
                    out.writeBytes(roomsToString());
                } else if (input.matches("USERS: (.*)")) {
                    Pattern pattern = Pattern.compile("USERS: (.*)");
                    Matcher matcher = pattern.matcher(input);
                    Room r = findRoomByName(matcher.group(1));
                    out.writeBytes(r.clientsToString());
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String roomsToString() {
        StringBuilder sb = new StringBuilder();
        for (Room r : Server.getRooms()) {
            sb.append(r.getName() + "\n");
        }
        return sb.toString();
    }

    public Room findRoomByName(String name) {
        for (Room r : Server.getRooms()) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }
}
