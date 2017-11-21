package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Room extends Thread {

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

    public void addClient(Client client) {
        clients.add(client);
    }

    public void sendMessage(Client client, String message) {
        try {
            for (DataOutputStream out : outputStreams) {

                out.writeBytes(client.getName() + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String clientsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (Client c : clients) {
            sb.append(c.getName() + "\n");
        }
        return sb.toString();
    }

}
