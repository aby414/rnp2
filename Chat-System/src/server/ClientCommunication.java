package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientCommunication extends Thread {

    private Room room;
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;
    private boolean quit = false;

    public ClientCommunication(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            registerUser();
            chooseLobbyCommands();
            sendMessage();
            Server.removeClient(this);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser() throws IOException {
        //Send hello message to ClientCommunication
        out.writeBytes("SELECTNAME \n");
        while (!quit) {
            String name = in.readLine();
            if (name == null) {
                return;
            }
            if (name.toUpperCase().equals("QUIT")) {
                quit = true;
            }
            synchronized (Server.getNames()) {
                if (!Server.getNames().contains(name)) {
                    Server.getNames().add(name);
                    this.setName(name);
                    System.out.println(name);
                    out.writeBytes("ACCEPTED \n");
                    break;
                } else {
                    out.writeBytes("INVALID \n");
                    out.writeBytes("SELECTNAME \n");
                }
            }

        }

    }

    private void chooseLobbyCommands() throws IOException {
        while (!quit) {
            String input = in.readLine();

            if (input == null) {
                return;
            }

            input = input.toUpperCase();
            Pattern userPattern = Pattern.compile("USERS: (.*)");
            Matcher userMatcher = userPattern.matcher(input);

            Pattern joinRoomPattern = Pattern.compile("JOIN: (.*)");
            Matcher joinRoomMatcher = joinRoomPattern.matcher(input);

            if (input.equals("ROOMS")) {
                out.writeBytes(roomsToString());
            } else if (userMatcher.find()) {
                Room r = findRoomByName(userMatcher.group(1));
                if (r != null) {
                    out.writeBytes(r.clientsToString() + " \n");
                } else {
                    out.writeBytes("INVALID_ROOMNAME \n");
                }
            } else if (input.equals("HELP")) {
                out.writeBytes("ROOMS \n");
                out.writeBytes("USERS: <ROOMNAME> \n");
                out.writeBytes("JOIN: <ROOMNAME> \n");
            } else if (joinRoomMatcher.find()) {
                Room r = findRoomByName(joinRoomMatcher.group(1));
                this.room = r;
                r.addClient(this);
                out.writeBytes("JOIN_SUCCESSFUL \n");
                room.outputStreams.add(out);
                room.sendClientJoinedNotification(this);
                break;
            } else if (input.equals("QUIT")) {
                quit = true;
            } else {
                out.writeBytes("UNKNOWN_COMMAND \n");
            }
        }
    }

    private void sendMessage() throws IOException {
        while (!quit) {
            String input = in.readLine();

            if (input == null) {
                return;
            }

            Pattern messagePattern = Pattern.compile("MESSAGE: (.*)");
            Matcher messageMatcher = messagePattern.matcher(input.toUpperCase());
            if (messageMatcher.find()) {
                Server.sendMessageToRoom(this, room, messageMatcher.group(1));
            } else if (input.equals("QUIT")) {
                quit = true;
            } else {
                out.writeBytes("INVALID_MESSAGE_FORMAT \n");
            }


        }
        out.writeBytes("BYE BYE \n");
    }

    public String roomsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (Room r : Server.getRooms()) {
            sb.append(r.getName() + "\n");
        }
        return sb.toString();
    }

    public Room findRoomByName(String name) {
        for (Room r : Server.getRooms()) {
            if (r.getName().toUpperCase().equals(name)) {
                return r;
            }
        }
        return null;
    }

    public Room getRoom() {
        return room;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public String clientsToString(Room r) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (ClientCommunication c : Server.getUsersForRoom(r.getName())) {
            sb.append(c.getName() + "\n");
        }
        return sb.toString();
    }
}
