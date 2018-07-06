import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class Core extends PApplet {

    public static boolean DEBUG = true;

    private ArrayList<Tile> tiles;
    private static int tilesX = 40;
    private static int tilesY = 30;

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
                tiles.add(new Tile(this, x, y, true));
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
        if (x < 0 || y < 0 || x > tilesX - 1 || y > tilesY - 1)
            return null;
        else return tiles.get(y + tilesY * x);
    }

    private Tile getRandomTile() {
        return getTile(floor(random(0, tilesX)), floor(random(0, tilesY)));
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
        if (mouseButton == RIGHT) {
            for (Tile t : clickedOn.getNeighbours()) {
                t.setColor(color(255, 0, 0));
                //System.out.println(t.toString());
            }
        }
    }

    private void generateMaze() {

        ArrayList<Tile> unvisitedTiles = new ArrayList<>(tiles);

        //Pick a cell, mark it as part of the maze. Add the walls of the cell to the wall list.
        while (!unvisitedTiles.isEmpty()) {
            Tile startTile = unvisitedTiles.get((floor(random(0, unvisitedTiles.size() - 1))));
            while (startTile != null) {
                unvisitedTiles.remove(startTile);
                startTile.setWall(false);
                startTile = startTile.getNeighbours().stream().filter(unvisitedTiles::contains).findAny().orElse(null);
            }
        }
    }
}