package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private final ConnectionHandler connection;
    private final Canvas canvas;
    private final GameField gameField;

    private final boolean isLeftPlayer;

    public GameWindow(ConnectionHandler connection, boolean isLeftPlayer) {
        this.connection = connection;
        this.isLeftPlayer = isLeftPlayer;
        this.gameField = new GameField(50, 50, this, isLeftPlayer); // Example dimensions, adjust as needed
        this.gameField.spawnAnimals(4, 2); // Example: 10 sheep and 5 wolves

        StackPane root = new StackPane();
        canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        if (isLeftPlayer){
            this.setTitle("Left Player");
        } else {
            this.setTitle("Right Player");
        }
        this.setScene(scene);
        drawField();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);

        // Send initial game state to the other player
        try {
            connection.sendObject(gameField.getAnimals());
        } catch (IOException e) {
            showError("Failed to send initial game state");
        }

        // Set up a timeline to update the game field periodically
        double v = 100;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(v), event -> {
            gameField.updateField();
            drawField();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void handleMouseClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            int x = (int) event.getX() / 10;
            int y = (int) event.getY() / 10;
            gameField.addWall(x, y);
        }
    }

//    private void listenForUpdates() {
//        try {
//            while (true) {
//                Object obj = connection.receiveObject();
//                if (obj instanceof ConcurrentHashMap) {
//                    //ConcurrentHashMap<Point, Animal> receivedAnimals = (ConcurrentHashMap<Point, Animal>) obj;
//                    //gameField.getAnimals().putAll(receivedAnimals);
//                    updateField();
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            showError("Connection lost");
//        }
//    }

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

        gc.setFill(Color.BLACK);
        for (Point wall : gameField.getWalls().keySet()) {
            gc.fillRect(wall.x * 10, wall.y * 10, 10, 10);
        }

        // Draw animals
        for (Animal animal : gameField.getAnimals().values()) {
            animal.draw(gc);
        }
    }

    public void addAnimalToOtherField(Animal animal, int x, int y) {
        try {
            connection.sendObject(animal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleReceivedAnimal(Animal animal) {
        animal.setGameField(gameField);
        gameField.addAnimal(animal);
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
