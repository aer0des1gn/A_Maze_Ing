package AMazeING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tile {

    public static List<Tile> unvistitedTiles = new ArrayList<>();
    public static List<Tile> vistitedTiles = new ArrayList<>();

    private int distance = Integer.MAX_VALUE;
    private Tile lastTile = null;

    private Maze maze;

    private int x;
    private int y;

    private boolean east = true;
    private boolean south = true;

    Tile(Maze maze, int x, int y) {
        this.maze = maze;
        this.x = x;
        this.y = y;
        unvistitedTiles.add(this);
    }

    public void setLast(Tile lastTile) {
        this.lastTile = lastTile;
    }

    public void setAsGoal() {
        this.distance = 0;
    }

    public int getDistance() {
        return distance;
    }

    public void vistited() {
        unvistitedTiles.remove(this);
        vistitedTiles.add(this);
    }

    public ArrayList<Tile> getOpenNeighbours() {
        ArrayList<Tile> tiles = new ArrayList<>();
        Tile temp;
        if ((temp = maze.getTile(x - 1, y)) != null && !temp.isEast()) tiles.add(temp); //west
        if (!east) tiles.add(maze.getTile(x + 1, y)); //east
        if ((temp = maze.getTile(x, y - 1)) != null && !temp.isSouth()) tiles.add(temp); //north
        if (!south) tiles.add(maze.getTile(x, y + 1)); //south
        tiles.removeAll(Collections.singleton(null));
        return tiles;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Tile getLastTile() {
        return lastTile;
    }

    public boolean isEast() {
        return east;
    }

    public boolean isSouth() {
        return south;
    }

    public boolean isWest() {
        return maze.getTile(x - 1, y) == null || maze.getTile(x - 1, y).isEast();
    }

    public boolean isNorth() {
        return maze.getTile(x, y - 1) != null && maze.getTile(x, y - 1).isSouth();
    }

    public Tile getNorth() {
        return maze.getTile(getX(), getY() - 1);
    }

    public Tile getEast() {
        return maze.getTile(getX() + 1, getY());
    }

    public Tile getSouth() {
        return maze.getTile(getX(), getY() + 1);
    }

    public Tile getWest() {
        return maze.getTile(getX() - 1, getY());
    }

    public Maze getMaze() {
        return maze;
    }

    public void draw() {
        int width = maze.getCore().WIDTH;

        if (maze.getFrontierCell().contains(this) || maze.getInCell().contains(this)) {
            maze.getCore().noStroke();
            if (maze.getInCell().contains(this))
                maze.getCore().fill(255);
            else
                maze.getCore().fill(255, 0, 0);
            maze.getCore().rect(x * width, y * width, width, width);
        }
    }

    public void drawWalls() {

        int width = maze.getCore().WIDTH;

        maze.getCore().stroke(0);
        if (east) {
            maze.getCore().line(
                    (x + 1) * width,
                    y * width,
                    (x + 1) * width,
                    (y + 1) * width);
        }
        if (south) {
            maze.getCore().line(
                    x * width,
                    (y + 1) * width,
                    (x + 1) * width,
                    (y + 1) * width);
        }
    }

    public String toString() {
        return x + "|" + y;
    }

    public ArrayList<Tile> getNeighbours() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(maze.getTile(x - 1, y));
        tiles.add(maze.getTile(x + 1, y));
        tiles.add(maze.getTile(x, y + 1));
        tiles.add(maze.getTile(x, y - 1));
        tiles.removeAll(Collections.singleton(null));
        return tiles;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void destroySouth() {
        this.south = false;
    }

    public void destroyEast() {
        this.east = false;
    }
}

