package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {

    String name;
    Server server;
    List<ClientCommunication> clients = new ArrayList<>();
    Set<PrintWriter> outputStreams = new HashSet<>();

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
            for (PrintWriter out : outputStreams) {

                out.println(client.getName() + ": " + message);
                out.flush();
            }
    }

    public void sendClientGoneMessage(ClientCommunication client) {
            for (PrintWriter out : outputStreams) {
                if (!out.equals(client.getOut())) {
                    out.println(client.getName() + " hat den Raum verlassen");
                    out.flush();
                }
            }
    }

    public void sendClientJoinedNotification(ClientCommunication client) {
            for (PrintWriter out : outputStreams) {
                if (!out.equals(client.getOut())) {
                    out.println(client.getName() + " hat den Raum betreten");
                    out.flush();
                }
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

    public Set<PrintWriter> getOutputStreams() {
        return outputStreams;
    }

    public String getName() {
        return name;
    }
}
