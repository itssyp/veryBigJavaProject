package network;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.Point;
import animals.Animal;

public class ConnectionHandler {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ConnectionHandler(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        setupStreams();
    }

    public ConnectionHandler(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        setupStreams();
    }

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }

    public Object receiveObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public void close() throws IOException {
        input.close();
        output.close();
        socket.close();
    }
}
