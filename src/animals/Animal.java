package animals;

import javafx.scene.canvas.GraphicsContext;
import game.GameField;
import java.io.Serializable;
import java.util.Random;

public abstract class Animal implements Runnable, Serializable {
    protected int x, y;
    protected boolean alive = true;
    protected final Random random = new Random();
    protected final int fieldWidth;
    protected final int fieldHeight;
    protected final GameField gameField;

    public Animal(int x, int y, int fieldWidth, int fieldHeight, GameField gameField) {
        this.x = x;
        this.y = y;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.gameField = gameField;
    }

    public abstract void move();

    public abstract void draw(GraphicsContext gc);

    protected void checkBoundary() {
        if (x < 0) {
            x = fieldWidth - 1;
            gameField.animalCrossedBoundary(this, Direction.LEFT);
        } else if (x >= fieldWidth) {
            x = 0;
            gameField.animalCrossedBoundary(this, Direction.RIGHT);
        } else if (y < 0) {
            y = fieldHeight - 1;
            gameField.animalCrossedBoundary(this, Direction.UP);
        } else if (y >= fieldHeight) {
            y = 0;
            gameField.animalCrossedBoundary(this, Direction.DOWN);
        }
    }

    @Override
    public void run() {
        while (alive) {
            move();
            checkBoundary(); // Check if the animal crossed a boundary
            try {
                Thread.sleep(1000); // Adjust sleep time as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public boolean isAlive() { return alive; }
    public void die() { alive = false; }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
