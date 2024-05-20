import gui.StartWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class SheepGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartWindow startWindow = new StartWindow();
        Scene scene = new Scene(startWindow, 400, 300);
        primaryStage.setTitle("Animal Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
