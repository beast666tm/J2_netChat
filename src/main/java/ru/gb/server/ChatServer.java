package ru.gb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer {

    private final Vector<ClientHandler> clients;
    private final AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public ChatServer() {
        clients = new Vector<>();
        authService = new SimpleAuthService();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                System.out.println("Wait client connection...");
                final Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                System.out.println("Client connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public boolean isNickBusy(String nick) {
//        return clients.equals(nick);
//    }

    public boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public void subscribe(ClientHandler ClientHandler) {
        clients.add(ClientHandler);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.getNick());
        broadcastClientsList();
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String msg) {
        if (from.getNick().equals(nickTo)){
            from.sendMessage("от " + from.getNick() + ": " + msg);
        return;
        }
        for (ClientHandler client : clients) {
            if (client.getNick().equals(nickTo)) {
                client.sendMessage("участнику " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMessage("Участника с ником " + nickTo + " нет в чат-комнате");
    }

    public void broadcastClientsList() {
        StringBuilder clientsCommand = new StringBuilder(clients.size());
        clientsCommand.append("/clients: ");
        for (ClientHandler client : clients) {
            clientsCommand.append(client.getNick()).append(" ");
        }
        clientsCommand.setLength(clientsCommand.length() - 1);
        broadcast(clientsCommand.toString());
        String out = clientsCommand.toString();
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(out);
        }
    }

    public void broadcast(String msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }
}
