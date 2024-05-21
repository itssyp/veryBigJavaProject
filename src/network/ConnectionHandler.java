package network;

import animals.Animal;
import gui.GameWindow;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private GameWindow gameWindow;

    public ConnectionHandler(Socket socket, GameWindow gameWindow) throws IOException {
        this.socket = socket;
        this.gameWindow = gameWindow;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

        // Start a thread to listen for incoming messages
        new Thread(this::listenForMessages).start();
    }

    private void listenForMessages() {
        try {
            while (true) {
                Object receivedObject = inputStream.readObject();
                if (receivedObject instanceof Animal) {
                    Animal receivedAnimal = (Animal) receivedObject;
                    if (gameWindow != null) {
                        gameWindow.handleReceivedAnimal(receivedAnimal);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) throws IOException {
        outputStream.writeObject(object);
        outputStream.flush();
    }
}
