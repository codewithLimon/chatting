package com.example.chatting;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
    private ArrayList<PrintWriter> clients;
    private ArrayList<String> clientNames;

    public ChatServer() {
        clients = new ArrayList<PrintWriter>();
        clientNames = new ArrayList<String>();
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clients.add(writer);
                Thread t = new Thread(new ClientHandler(clientSocket, writer));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void broadcast(String message, String senderName) {
        for (PrintWriter client : clients) {
            client.println(senderName + ": " + message);
            client.flush();
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start(1234);
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String clientName;

        public ClientHandler(Socket socket, PrintWriter writer) {
            clientSocket = socket;
            this.writer = writer;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientName = reader.readLine();
                clientNames.add(clientName);
                broadcast(clientName + " has joined the chat", "SERVER");
                System.out.println(clientName + " has joined the chat");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    broadcast(message, clientName);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                clients.remove(writer);
                clientNames.remove(clientName);
                broadcast(clientName + " has left the chat", "SERVER");
                System.out.println(clientName + " has left the chat");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}
