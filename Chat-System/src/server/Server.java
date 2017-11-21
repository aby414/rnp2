package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.*;

public class Server {

    private static final int PORT = 9001;

    private DataOutputStream dataOutputStream;
    private BufferedReader bufferedReader;

    private static Set<String> names = new HashSet<String>(Arrays.asList("TIMO"));
    private static List<Room> rooms = new ArrayList<>();

    public Server(){
        Room room = new Room("chat1",this);
        Room room1 = new Room("chat2",this);
        rooms.add(room);
        rooms.add(room1);
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started: " + serverSocket);
        Server server = new Server();
        try {
            while(true){
                new Client(serverSocket.accept()).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    public void createRoom(String roomName){
        Room room = new Room(roomName, this);
        rooms.add(room);
        room.start();
    }

    public List getUsersForRoom(String roomName){
        for (Room r: rooms) {
            if(r.getName().equals(roomName)){
                return r.getClients();
            }
        }
        throw new IllegalArgumentException("The chatroom " + roomName +" does not exist!");
    }

    public static void sendMessageToRoom(Client client, Room room, String message){
        room.sendMessage(client,message);
    }

    public static List<Room> getRooms() {
        return rooms;
    }

    public static Set getNames(){
        return names;
    }


    public static void removeClient(Client client){
        if(client.getName() != null){
            names.remove(client.getName());
        }
        if(client.getOut() != null && client.getRoom() != null){
            client.getRoom().sendClientGoneMessage(client);
            client.getRoom().getOutputStreams().remove(client.getOut());
        }
        if(client != null && client.getRoom() != null){
            client.getRoom().getClients().remove(client);
        }

    }
}
