package main.java;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 33290;
    private ServerSocket server;
    private DataInputStream dataInputStream;
    private static final String STOP_COMMAND = "STOP";


    public Server() {
        try{
            server = new ServerSocket(PORT);
            System.out.println("Server started. Server Port: " + getPort());
            System.out.println("Waiting for connection...");
            initConnections();
            System.out.println("Waiting for clients...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return server.getLocalPort();
    }

    private void initConnections() throws IOException {
        Socket clientSocket = server.accept();
        dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        readMessages();
        close();
    }

    private void close() throws IOException {
        dataInputStream.close();
        server.close();
    }

    private void readMessages() throws IOException {
        String line = "";
        while(!line.equals(STOP_COMMAND)) {
            line = dataInputStream.readUTF();
            System.out.println("New message:");
            System.out.println(line);
        }
    }
}
