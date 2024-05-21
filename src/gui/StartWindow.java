package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Animal Game Start Window");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label portLabel = new Label("Port:");
        grid.add(portLabel, 0, 0);

        TextField portTextField = new TextField("12345");
        grid.add(portTextField, 1, 0);

        Button hostButton = new Button("Host as Left Player");
        grid.add(hostButton, 0, 1);

        Button connectButton = new Button("Connect as Right Player");
        grid.add(connectButton, 1, 1);

        Label ipLabel = new Label("IP Address:");
        grid.add(ipLabel, 0, 2);

        TextField ipTextField = new TextField("localhost");
        grid.add(ipTextField, 1, 2);

        hostButton.setOnAction(event -> {
            int port = Integer.parseInt(portTextField.getText());
            new Thread(() -> {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    Socket socket = serverSocket.accept();
                    Platform.runLater(() -> {
                        try {
                            GameWindow gameWindow = null;
                            ConnectionHandler coni = new ConnectionHandler(socket, null);
                            gameWindow = new GameWindow(coni, true);
                            gameWindow.setConWin();
                            gameWindow.show();
                            primaryStage.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        connectButton.setOnAction(event -> {
            String ipAddress = ipTextField.getText();
            int port = Integer.parseInt(portTextField.getText());
            new Thread(() -> {
                try {
                    Socket socket = new Socket(ipAddress, port);
                    Platform.runLater(() -> {
                        try {
                            GameWindow gameWindow = null;
                            ConnectionHandler coni = new ConnectionHandler(socket, null);
                            gameWindow = new GameWindow(coni, false);
                            gameWindow.setConWin();
                            gameWindow.show();
                            primaryStage.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
