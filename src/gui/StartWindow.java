package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.ConnectionHandler;

import java.io.IOException;

public class StartWindow extends VBox {
    public StartWindow() {
        Label ipLabel = new Label("IP Address:");
        TextField ipField = new TextField("localhost");
        Label portLabel = new Label("Port:");
        TextField portField = new TextField("12345");
        Button connectButton = new Button("Connect");
        Button waitButton = new Button("Wait for Connection");

        connectButton.setOnAction(event -> connectToServer(ipField.getText(), Integer.parseInt(portField.getText())));
        waitButton.setOnAction(event -> waitForConnection(Integer.parseInt(portField.getText())));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(ipLabel, 0, 0);
        gridPane.add(ipField, 1, 0);
        gridPane.add(portLabel, 0, 1);
        gridPane.add(portField, 1, 1);
        gridPane.add(connectButton, 0, 2);
        gridPane.add(waitButton, 1, 2);

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(gridPane);
    }

    private void connectToServer(String ipAddress, int port) {
        try {
            ConnectionHandler connection = new ConnectionHandler(ipAddress, port);
            new GameWindow(connection).show();
            ((Stage) getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Connection failed");
        }
    }

    private void waitForConnection(int port) {
        try {
            ConnectionHandler connection = new ConnectionHandler(port);
            new GameWindow(connection).show();
            ((Stage) getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Waiting for connection failed");
        }
    }

    private void showError(String message) {
        // Show error dialog
    }
}
