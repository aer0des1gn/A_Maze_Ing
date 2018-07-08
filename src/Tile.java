import java.util.ArrayList;
import java.util.Collections;

public class Tile {

    private Core core;

    private int x;
    private int y;

    private Wall east;
    private Wall south;

    Tile(Core core, int x, int y) {
        this.core = core;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        if (core.frontierCell.contains(this) || core.inCell.contains(this)) {
            core.noStroke();
            if (core.inCell.contains(this))
                core.fill(255);
            else
                core.fill(255, 0, 0);
            core.rect(x * core.WIDTH, y * core.WIDTH, core.WIDTH, core.WIDTH);
        }
    }

    public void drawWalls() {
        core.stroke(0);
        if (getEast().isExisting()) {
            core.line((x + 1) * core.WIDTH, y * core.WIDTH, (x + 1) * core.WIDTH, (y + 1) * core.WIDTH);
        }
        if (getSouth().isExisting()) {
            core.line(x * core.WIDTH, (y + 1) * core.WIDTH, (x + 1) * core.WIDTH, (y + 1) * core.WIDTH);
        }
    }

    public String toString() {
        return x + "|" + y;
    }

    public ArrayList<Tile> getNeighbours() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(core.getTile(x - 1, y));
        tiles.add(core.getTile(x + 1, y));
        tiles.add(core.getTile(x, y + 1));
        tiles.add(core.getTile(x, y - 1));
        tiles.removeAll(Collections.singleton(null));
        return tiles;
    }

    public Wall getEast() {
        return east == null ? (east = new Wall(this, core.getTile(x + 1, y))) : east;
    }

    public Wall getSouth() {
        return south == null ? (south = new Wall(this, core.getTile(x, y + 1))) : south;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

