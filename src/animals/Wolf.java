package animals;

import game.GameField;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wolf extends Animal {
    public Wolf(int x, int y, GameField gameField) {
        super(x, y, gameField);
    }

    @Override
    public void move() {
        // Randomly move sheep to adjacent position
        if (!alive) return;
        int dx = 1;
        int dy = 0;
        if (!gameField.isWall(x + dx, y + dy)) {
            x = x + dx; // Ensure x stays within bounds
            y = Math.max(0, Math.min(gameField.getHeight(), y + dy)); // Ensure y stays within bounds
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!alive) return;
        gc.setFill(Color.GRAY);
        gc.fillOval(x * 10, y * 10, 10, 10); // Example drawing logic
    }
}