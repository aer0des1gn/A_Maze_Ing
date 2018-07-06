import processing.core.PConstants;

import java.util.ArrayList;
import java.util.Collections;

public class Tile {

    private Core core;

    public static final int WIDTH = 30;

    private int x;
    private int y;

    private int color = 255;

    boolean isWall;

    Tile(Core core, int x, int y, boolean isWall) {
        this.core = core;
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    public void draw() {
        if (isWall) core.fill(0);
            //farbe feld
        else core.fill(color);
        core.rect(x * WIDTH, y * WIDTH, WIDTH, WIDTH);
        //farbe schrift
        core.fill(0);
        if (Core.DEBUG) {
            core.textAlign(PConstants.LEFT, PConstants.TOP);
            core.text(toString(), x * WIDTH, y * WIDTH);
        }
    }

    public String toString() {
        return x + "|" + y;
    }

    public ArrayList<Tile> getNeighbours() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(core.getTile(x - 2, y));
        tiles.add(core.getTile(x + 2, y));
        tiles.add(core.getTile(x, y + 2));
        tiles.add(core.getTile(x, y - 2));
        tiles.removeAll(Collections.singleton(null));
        return tiles;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
