import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Core extends PApplet {

    public static boolean DEBUG = false;

    private ArrayList<Tile> tiles;
    private int tilesX = 10;
    private int tilesY = 11;
    public int WIDTH;

    public static void main(String[] args) {
        PApplet.main("Core", args);
    }

    public void settings() {
        size(601, 601);
        WIDTH = width / tilesX;
        System.out.println(WIDTH);
    }

    public void setup() {
        frameRate(20);
        tiles = new ArrayList<>();
        for (int x = 0; x < tilesX; x++)
            for (int y = 0; y < tilesY; y++)
                tiles.add(new Tile(this, x, y));
    }

    public void draw() {
        background(190);
        surface.setTitle("Amazeing, FPS: " + round(frameRate));
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
            setup();
            generateMaze();
        }
        if (key == 'd') {
            DEBUG = !DEBUG;
        }
    }

    private void removeWall(Tile a, Tile b) {
        if (a.getX() < b.getX()) {
            a.getEast().setExisting(false);
        } else if (a.getX() > b.getX()) {
            a.getWest().setExisting(false);
        } else if (a.getY() < b.getY()) {
            a.getSouth().setExisting(false);
        } else if (a.getY() > b.getY()) {
            a.getNorth().setExisting(false);
        }
        }


    private void generateMaze() {
        ArrayList<Tile> frontier = new ArrayList<>();
        Tile tile = getRandomTile();
        tile.setInMaze(true);
        tile.setVisited();
        tile.getNeighbours().stream().filter(Tile::isUnvisited).forEach(each -> {
            frontier.add(each);
            System.out.println("adding..");
            each.setInMaze(true);
        });
        while (!frontier.isEmpty()) {
            tile = frontier.get(floor(random(frontier.size())));
            List<Tile> tempTiles = tile.getNeighbours().stream().filter(each -> !each.isUnvisited()).collect(Collectors.toList());
            if (!tempTiles.isEmpty()) removeWall(tile, tempTiles.get(floor(random(tempTiles.size()))));
            else continue;
            tile.getNeighbours().stream().filter(Tile::isUnvisited).forEach(each -> {
                frontier.add(each);
                System.out.println("adding...");
                each.setInMaze(true);
            });
            tile.setVisited();
            frontier.remove(tile);
        }
    }
}