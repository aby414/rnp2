package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {

    String name;
    Server server;
    List<ClientCommunication> clients = new ArrayList<>();
    Set<DataOutputStream> outputStreams = new HashSet<>();

    public Room(String name, Server server) {
        this.name = name;
        this.server = server;
    }

    public List<ClientCommunication> getClients() {
        return clients;
    }

    public void addClient(ClientCommunication client) {
        clients.add(client);
    }

    public void sendMessage(ClientCommunication client, String message) {
        try {
            for (DataOutputStream out : outputStreams) {

                out.writeBytes(client.getName() + ": " + message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientGoneMessage(ClientCommunication client) {
        try {
            for (DataOutputStream out : outputStreams) {
                if (!out.equals(client.getOut())) {
                    out.writeBytes(client.getName() + " hat den Raum verlassen \n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientJoinedNotification(ClientCommunication client) {
        try {
            for (DataOutputStream out : outputStreams) {
                if (!out.equals(client.getOut())) {
                    out.writeBytes(client.getName() + " hat den Raum betreten \n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String clientsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (ClientCommunication c : clients) {
            sb.append(c.getName() + "\n");
        }
        return sb.toString();
    }

    public Set<DataOutputStream> getOutputStreams() {
        return outputStreams;
    }

    public String getName() {
        return name;
    }
}
