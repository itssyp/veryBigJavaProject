package animals;

import game.GameField;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.Random;

public abstract class Animal implements Runnable, Serializable {
    protected int x, y;
    protected boolean alive = true;
    protected final Random random = new Random();

    protected transient GameField gameField;

    public Animal(int x, int y, GameField gameField) {
        this.x = x;
        this.y = y;
        this.gameField = gameField;
    }

    public abstract void move();

    public abstract void draw(GraphicsContext gc);

    protected void checkBoundary() {
        int fieldWidth = gameField.getWidth();

        if (x < 0) {
            gameField.animalCrossedBoundary(this, Direction.LEFT);
        } else if (x >= fieldWidth) {
            gameField.animalCrossedBoundary(this, Direction.RIGHT);
        }
    }

    @Override
    public void run() {
        while (alive) {
            move();
            checkBoundary();
            try {
                Thread.sleep(100); // Adjust sleep time as needed
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

    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }

    public enum Direction {
        LEFT, RIGHT
    }
}
