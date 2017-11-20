package server;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Room extends Thread{

    Server server;
    List<Client> clients = new ArrayList<>();
    HashSet<DataOutputStream> outputStreams = new HashSet<>();

    public Room(String name, Server server) {
        super(name);
        this.server = server;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClient(Client client){
        clients.add(client);
    }

    public void sendMessage(String message){

    }

}
