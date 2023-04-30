package com.example.chatting;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient {
    private String name;
    private BufferedReader reader;
    private PrintWriter writer;

    public ChatClient(String name) {
        this.name = name;
    }

    public void start(String hostname, int port) {
        try {
            Socket socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            writer.println(name);
            writer.flush();
            Thread t = new Thread(new ServerHandler());
            t.start();
            Scanner scanner = new Scanner(System.in);
            String message;
            while ((message = scanner.nextLine()) != null) {
                writer.println(message);
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        ChatClient client = new ChatClient(name);
        client.start("localhost", 1234);
    }

    private class ServerHandler implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
