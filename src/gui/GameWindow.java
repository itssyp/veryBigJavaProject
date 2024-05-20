package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.ConnectionHandler;
import game.GameField;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.Point;
import animals.Animal;

public class GameWindow extends Stage {
    private ConnectionHandler connection;
    private Canvas canvas;
    private GameField gameField;

    public GameWindow(ConnectionHandler connection) {
        this.connection = connection;
        this.gameField = new GameField(50, 50, this); // Example dimensions, adjust as needed
        this.gameField.spawnAnimals(4, 2); // Example: 10 sheep and 5 wolves

        StackPane root = new StackPane();
        canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        this.setTitle("Animal Game");
        this.setScene(scene);
        drawField();

        // Send initial game state to the other player
        try {
            connection.sendObject(gameField.getAnimals());
        } catch (IOException e) {
            showError("Failed to send initial game state");
        }

        // Start a thread to listen for updates from the other player
        new Thread(this::listenForUpdates).start();

        // Set up a timeline to update the game field periodically
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateGameField()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void listenForUpdates() {
        try {
            while (true) {
                Object obj = connection.receiveObject();
                if (obj instanceof ConcurrentHashMap) {
                    ConcurrentHashMap<Point, Animal> receivedAnimals = (ConcurrentHashMap<Point, Animal>) obj;
                    //gameField.getAnimals().putAll(receivedAnimals);
                    updateField();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            showError("Connection lost");
        }
    }

    public void addAnimalToOtherField(Animal animal, int x, int y) {
        // Send the animal to the other player's field
        // You can send it over the network to the other player's game instance
        // For example: connection.sendObject(animal);
        // Then remove the animal from this player's field
        gameField.getAnimals().remove(new Point(animal.getX(), animal.getY()));
        // Add the animal to the other player's field
        animal.setX(x);
        animal.setY(y);
        gameField.addAnimal(animal);
    }

    private void updateGameField() {
        gameField.updateField();
        drawField();

        // Send updated game state to the other player
        try {
            connection.sendObject(gameField.getAnimals());
        } catch (IOException e) {
            showError("Failed to send game state update");
        }
    }

    private void drawField() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw animals
        for (Animal animal : gameField.getAnimals().values()) {
            animal.draw(gc);
        }
    }

    public void updateField() {
        drawField();
        // Redraw the field with the current game state
    }

    private void showError(String message) {
        // Show error dialog
        System.err.println(message);
    }
}
