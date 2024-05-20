import gui.StartWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class SheepGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartWindow startWindow = new StartWindow();
        startWindow.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
