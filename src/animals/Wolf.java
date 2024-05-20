package animals;

import game.GameField;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wolf extends Animal {
    public Wolf(int x, int y, int fieldWidth, int fieldHeight, GameField gameField) {
        super(x, y, fieldWidth, fieldHeight, gameField);
    }

    @Override
    public void move() {
        // Randomly move sheep to adjacent position
        if (!alive) return;
        int dx = random.nextInt(3);
        int dy = random.nextInt(3);
        //x = Math.max(0, x + dx); // Ensure x stays within bounds
        //y = Math.max(0, y + dy); // Ensure y stays within bounds
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!alive) return;
        gc.setFill(Color.GRAY);
        gc.fillOval(x * 10, y * 10, 10, 10); // Example drawing logic
    }
}