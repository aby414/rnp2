package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Server {

    private static final int PORT = 9001;

    private DataOutputStream dataOutputStream;
    private BufferedReader bufferedReader;

    private static HashSet<Client> clients = new HashSet<>();
    private static List<Room> rooms = new ArrayList<>();

    public Server(){

    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started: " + serverSocket);
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

    public void sendMessage(String room){

    }

    public static List<Room> getRooms() {
        return rooms;
    }
}
