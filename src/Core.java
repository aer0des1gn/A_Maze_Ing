import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Core extends PApplet {

    private ArrayList<Tile> tiles;
    private int tilesX = 15;
    private int tilesY = 15;
    public int WIDTH = 50;

    public static void main(String[] args) {
        PApplet.main("Core", args);
    }

    public void settings() {
        size(tilesX * WIDTH, tilesY * WIDTH);
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
        if (generate && !frontierCell.isEmpty()) {
            Tile cF = frontierCell.get(floor(random(frontierCell.size())));
            List<Tile> CIs = cF.getNeighbours().stream().filter(inCell::contains).collect(Collectors.toList());
            Tile cI = CIs.get(floor(random(CIs.size())));
            inCell.add(cF);
            cF.getNeighbours().forEach(each -> {
                if (!inCell.contains(each) && !frontierCell.contains(each))
                    frontierCell.add(each);
            });
            removeWall(cF, cI);
            frontierCell.remove(cF);
        } else
            generate = false;

        surface.setTitle("Amazeing, FPS: " + round(frameRate));
        for (Tile t : tiles)
            t.draw();
        for (Tile t : tiles)
            t.drawWalls();
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x > tilesX - 1 || y > tilesY - 1)
            return null;
        else return tiles.get(y + tilesY * x);
    }

    private Tile getRandomTile() {
        return getTile(floor(random(tilesX)), floor(random(tilesY)));
    }

    public void keyPressed() {
        if (key == 'g')
            generateMaze();
    }

    private void removeWall(Tile a, Tile b) {
        if (a.getX() < b.getX()) {
            a.getEast().setExisting(false);
        } else if (a.getX() > b.getX()) {
            b.getEast().setExisting(false);
        } else if (a.getY() < b.getY()) {
            a.getSouth().setExisting(false);
        } else if (a.getY() > b.getY()) {
            b.getSouth().setExisting(false);
        }
        }

    public ArrayList<Tile> inCell = new ArrayList<>();
    public ArrayList<Tile> frontierCell = new ArrayList<>();

    private boolean generate = false;

    private void generateMaze() {

        setup();
        inCell.clear();
        frontierCell.clear();

        Tile startTile = getRandomTile();
        inCell.add(startTile);
        frontierCell.addAll(startTile.getNeighbours());

        generate = true;

    }
}