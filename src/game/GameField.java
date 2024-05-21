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
    private final ConcurrentHashMap<Point, Boolean> walls = new ConcurrentHashMap<>();
    private final int width;
    private final int height;
    private final Random random = new Random();

    private transient GameWindow gameWindow;

    private final boolean isLeftPlayer; // To differentiate between left and right players

    public GameField(int width, int height, GameWindow gameWindow, boolean isLeftPlayer) {
        this.width = width;
        this.height = height;
        this.gameWindow = gameWindow;
        this.isLeftPlayer = isLeftPlayer;
        System.out.println(isLeftPlayer);
    }

    public void addAnimal(Animal animal) {
        Point position = new Point(animal.getX(), animal.getY());
        animal.resurrect();
        System.out.println("UJ ALLAT GECI");
        animals.put(position, animal);
        //gameWindow.updateField()
        new Thread(animal).start(); // Start a new thread for each animal
    }

    public void addWall(int x, int y) {
        Point position = new Point(x, y);
        walls.put(position, true);
    }

    public boolean isWall(int x, int y) {
        return walls.containsKey(new Point(x, y));
    }

    public void animalCrossedBoundary(Animal animal, Animal.Direction direction) {
        if (direction == Animal.Direction.LEFT && isLeftPlayer){
            animal.setX(0);
            return;
        }
        if (direction == Animal.Direction.RIGHT && !isLeftPlayer){
            animal.setX(width - 1);
            return;
        }
        Point cur = new Point(animal.getX(), animal.getY());

        animal.die();
        if ((direction == Animal.Direction.LEFT && !isLeftPlayer) || (direction == Animal.Direction.RIGHT && isLeftPlayer)) {
            int newX = (direction == Animal.Direction.LEFT) ? width - 1 : 0;
            animal.setX(newX);
            System.out.println(newX);
            gameWindow.addAnimalToOtherField(animal, newX, animal.getY());
        }
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
        //if (!isLeftPlayer)
        for (int i = 0; i < numSheep; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Sheep sheep = new Sheep(x, y, this);
            addAnimal(sheep);
            //new Thread(sheep).start();
        }

        for (int i = 0; i < numWolves; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Wolf wolf = new Wolf(x, y, this);
            addAnimal(wolf);
            //new Thread(wolf).start();
        }
    }

    public ConcurrentHashMap<Point, Animal> getAnimals() {
        return animals;
    }

    public ConcurrentHashMap<Point, Boolean> getWalls() {
        return walls;
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }


}
