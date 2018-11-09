package AMazeING;

import Evolution.EvolutionClass;
import processing.core.PApplet;

import java.util.ArrayList;

public class Core extends PApplet {

    private Maze actualMaze = null;

    private EvolutionClass evo = null;

    private int nextMaze = 0;
    private ArrayList<Maze> mazes = new ArrayList<>();

    private static boolean drawMaze = true;
    public static boolean debug = false;
    private static boolean changeFPS = false;

    public static int tiles = 20;
    public int WIDTH = 750 / tiles;

    public static void main(String[] args) {
        PApplet.main("AMazeING.Core");
    }

    public Core() {
        int mazesToGenerate = 10;
        for (int i = 0; i < mazesToGenerate; i++)
            mazes.add(new Maze(this));
    }

    public void settings() {
        size(tiles * WIDTH, tiles * WIDTH);
    }

    public void setup() {
        frameRate(1000);
    }

    public void draw() {

        if (actualMaze == null) {
            getNextMaze();
            return;
        }

        if (evo == null)
            evo = new EvolutionClass(this);

        evo.doStep();

        background(190);

        if (!drawMaze)
            return;

        surface.setTitle("A_MAZE_I_NG, FPS: " + round(frameRate));

        actualMaze.getTiles().forEach(Tile::draw);

        stroke(255, 0, 0);

        fill(255, 0, 0);

        //entrance
        rect(0, actualMaze.getEntrance().
                getY() * WIDTH, WIDTH, WIDTH);

        //exit
        rect(actualMaze.getExit().
                getX() * WIDTH, actualMaze.getExit().
                getY() * WIDTH, WIDTH, WIDTH);

        actualMaze.getTiles().forEach(Tile::drawWalls);

        //frame

        noFill();

        stroke(0);

        rect(0, 0, tiles * WIDTH - 1, tiles * WIDTH - 1);

        stroke(255, 0, 0);

        Tile current = actualMaze.getEntrance();

        int x;
        int y;
        while (current != null && current.getLastTile() != null) {
            x = (current.getX() + 1) * WIDTH - (WIDTH / 2);
            y = (current.getY() + 1) * WIDTH - (WIDTH / 2);
            line(x, y,
                    (current.getLastTile().getX() + 1) * WIDTH - (WIDTH / 2),
                    (current.getLastTile().getY() + 1) * WIDTH - (WIDTH / 2));
            current = current.getLastTile();
        }

        stroke(0, 0, 255);
        
        evo.draw();

    }

    public boolean isDraw() {
        return drawMaze;
    }

    public void keyPressed() {
        if (key == 'm')
            drawMaze = !drawMaze;
        if (key == 'd')
            debug = !debug;
        if (key == 'f') {
            changeFPS = !changeFPS;
            if (changeFPS) frameRate(10);
            else frameRate(1000);
        }
    }

    private void getNextMaze() {
        if (nextMaze >= mazes.size()) {
            nextMaze = 0;
            actualMaze = null;
        }
        actualMaze = mazes.get(nextMaze++);
    }

    public Maze getActualMaze() {
        return actualMaze;
    }
}