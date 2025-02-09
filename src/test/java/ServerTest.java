package test.java;

import main.java.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import static org.junit.jupiter.api.Assertions.*;
class ServerTest {
    private Server server;
    private Thread serverThread;
    private final int serverPort = 33290;

    @BeforeEach
    void startServer() throws InterruptedException {
        serverThread = new Thread(() -> server = new Server());
        serverThread.start();
    }

    @AfterEach
    void stopServer() {
        System.out.println("Stopping server");
        serverThread.interrupt();
    }

    @Test
    void testServerIsRunning() {
        assertDoesNotThrow(() -> new Socket("127.0.0.1", serverPort), "Server is not listening on the expected port!");
    }

    @Test
    void testServerPortIsBlocked() {
        ServerSocket testSocket = null;
        try {
            testSocket = new ServerSocket(serverPort);
            fail("Expected BindException, but no exception was thrown!");
        } catch (BindException e) {
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        } finally {
            if (testSocket != null && !testSocket.isClosed()) {
                System.out.println("Closing test socket");
            }
        }
    }

    @Test
    void testServerReceivesMessage() throws IOException {
        try (Socket client = new Socket("127.0.0.1", serverPort);
             DataOutputStream out = new DataOutputStream(client.getOutputStream());
             DataInputStream in = new DataInputStream(client.getInputStream())) {

            out.writeUTF("hello123");  // Wysyłamy wiadomość
            out.flush();
            out.writeUTF("STOP");
            out.flush();
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    void testServerStopsOnStopCommand() throws IOException {
        try (Socket client = new Socket("127.0.0.1", serverPort);
             DataOutputStream out = new DataOutputStream(client.getOutputStream())) {

            out.writeUTF("STOP");
            out.flush();
        }
    }
}
