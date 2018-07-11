import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Core extends PApplet {

    private static boolean DEBUG = false;

    private ArrayList<Tile> tiles;
    private int tilesX = 15;
    private int tilesY = 15;
    public int WIDTH = 50;

    private int entrance;
    private int exit;

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
        entrance = floor(random(tilesY - 1));
        exit = floor(random(tilesY - 1));
    }

    public void draw() {

        background(190);

        if (DEBUG && generate)
            generateCell();
        surface.setTitle("Amazeing, FPS: " + round(frameRate));
        tiles.forEach(Tile::draw);
        tiles.forEach(Tile::drawWalls);

        //frame

        noFill();
        stroke(0);
        rect(0, 0, tilesX * WIDTH -1, tilesY * WIDTH-1);

        stroke(255, 0, 0);

        //entrance
        line(0, entrance * WIDTH, 0, (entrance + 1) * WIDTH);

        //exit
        line(tilesX * WIDTH - 1, exit * WIDTH, tilesX * WIDTH - 1, (exit + 1) * WIDTH);
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
        else if (key == 'd')
            DEBUG = !DEBUG;
    }

    private void removeWall(Tile a, Tile b) {
        if (a.getX() < b.getX())
            a.destroyEast();
        else if (a.getX() > b.getX())
            b.destroyEast();
        else if (a.getY() < b.getY())
            a.destroySouth();
        else if (a.getY() > b.getY())
            b.destroySouth();
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

        long startTime = System.nanoTime();
        while (!frontierCell.isEmpty() && !DEBUG) {
            generateCell();
        }
        System.out.println("Duration in milli sec: " + ((System.nanoTime() - startTime) / 100000D));
    }

    private void generateCell() {
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
    }

}