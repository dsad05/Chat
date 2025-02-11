package main.java;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private static final int PORT = 33290;
    private static final String HOST = "localhost";
    private Socket socket;
    private static final String STOP_COMMAND = "STOP";
    private DataOutputStream dataOutputStream;


    public Client() {
        try {
            System.out.println("Connecting to " + HOST);
            this.socket = new Socket(HOST, PORT);
            sendMessages();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessages() throws IOException {
        String line;
        Scanner scanner = new Scanner(System.in);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        while (true) {
            line = scanner.nextLine();
            dataOutputStream.writeUTF(line);
            dataOutputStream.flush();
            if (Objects.equals(line, STOP_COMMAND)) {
                System.out.println("Got STOP message");
                close();
                break;
            }
        }
    }
    private void close() throws IOException {
        System.out.println("Closing connection...");
        dataOutputStream.close();
        socket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

}

