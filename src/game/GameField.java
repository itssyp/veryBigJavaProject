package game;

import animals.Animal;
import animals.Sheep;
import animals.Wolf;
import gui.GameWindow;

import java.awt.Point;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

public class GameField {
    private ConcurrentHashMap<Point, Animal> animals = new ConcurrentHashMap<>();
    private final int width;
    private final int height;
    private final Random random = new Random();
    private final GameWindow gameWindow;

    public GameField(int width, int height, GameWindow gameWindow) {
        this.width = width;
        this.height = height;
        this.gameWindow = gameWindow;
    }

    public void addAnimal(Animal animal) {
        Point position = new Point(animal.getX(), animal.getY());
        animals.put(position, animal);
        new Thread(animal).start(); // Start a new thread for each animal
    }

    public void animalCrossedBoundary(Animal animal, Animal.Direction direction) {
        int newX = animal.getX();
        int newY = animal.getY();

        switch (direction) {
            case LEFT:
                newX = width - 1;
                break;
            case RIGHT:
                newX = 0;
                break;
            case UP:
                newY = height - 1;
                break;
            case DOWN:
                newY = 0;
                break;
        }

        animal.setX(newX);
        animal.setY(newY);

        gameWindow.addAnimalToOtherField(animal, newX, newY);
    }

    public void updateField() {
        ConcurrentHashMap<Point, Animal> newAnimals = new ConcurrentHashMap<>();

        for (Animal animal : animals.values()) {
            if (!animal.isAlive()) continue;
            Point newPosition = new Point(animal.getX(), animal.getY());

            // Handle wolf eating sheep
            if (animal instanceof Wolf && newAnimals.containsKey(newPosition)) {
                Animal other = newAnimals.get(newPosition);
                if (other instanceof Sheep) {
                    other.die();
                    //newAnimals.remove(newPosition);
                }
            }

            newAnimals.put(newPosition, animal);
        }

        animals = newAnimals;
    }

    public void spawnAnimals(int numSheep, int numWolves) {
        for (int i = 0; i < numSheep; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            addAnimal(new Sheep(x, y, width, height, this));
        }

        for (int i = 0; i < numWolves; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            addAnimal(new Wolf(x, y, width, height, this));
        }
    }

    public ConcurrentHashMap<Point, Animal> getAnimals() {
        return animals;
    }
}
