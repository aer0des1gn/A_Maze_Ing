import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class Core extends PApplet {

    public static boolean DEBUG = true;

    private ArrayList<Tile> tiles;
    int tilesX = 40;
    int tilesY = 30;

    public static void main(String[] args) {
        PApplet.main("Core", args);
    }

    public void settings() {
        size(tilesX * Tile.WIDTH, tilesY * Tile.WIDTH);
    }

    public void setup() {
        frameRate(30);
        tiles = new ArrayList<>();
        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                tiles.add(new Tile(this, x, y, false));
            }
        }
    }

    public void draw() {
        background(255);
        surface.setTitle("A-Maze-Ing, FPS: " + round(frameRate));
        if (!DEBUG)
            noStroke();
        else stroke(0);
        for (Tile t : tiles) {
            t.draw();
        }
    }

    public Tile getTile(int x, int y) {
        if (x > 0 || y > 0 || x < tilesX - 1 || y < tilesY - 1)
            return tiles.get(y + tilesY * x);
        else return null;
    }

    public void keyPressed() {
        if (key == 'g') {
            generateMaze();
        }
        if (key == 'd') {
            DEBUG = !DEBUG;
        }
    }

    public void mouseClicked() {
        Tile clickedOn = getTile(floor(mouseX / Tile.WIDTH), floor(mouseY / Tile.WIDTH));
        System.out.println(clickedOn.toString());
        if (mouseButton == RIGHT) {
            for (Tile t : clickedOn.getNeighbours()) {
                t.setColor(color(255,0,0));
                //System.out.println(t.toString());
            }
        }
    }

    public void generateMaze() {

    }
}