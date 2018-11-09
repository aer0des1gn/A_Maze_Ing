package AMazeING;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Maze {

    private Core core;

    Maze(Core core) {
        this.core = core;
        generateMaze();
    }

    private ArrayList<Tile> tiles;

    private Tile entrance;
    private Tile exit;

    private int minTiles;

    private ArrayList<Tile> inCell = new ArrayList<>();
    private ArrayList<Tile> frontierCell = new ArrayList<>();

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

    private void generateMaze() {
        tiles = new ArrayList<>();
        for (int x = 0; x < Core.tiles; x++)
            for (int y = 0; y < Core.tiles; y++)
                tiles.add(new Tile(this, x, y));
        entrance = getTile(0, 0);
        exit = getTile(Core.tiles - 1, Core.tiles - 1);
        exit.setAsGoal();

        Tile startTile = getRandomTile();

        inCell.add(startTile);
        frontierCell.addAll(startTile.getNeighbours());

        while (!frontierCell.isEmpty()) {
            Tile cF = frontierCell.get(Core.floor(core.random(frontierCell.size())));
            List<Tile> CIs = cF.getNeighbours().stream().filter(inCell::contains).collect(Collectors.toList());
            Tile cI = CIs.get(Core.floor(core.random(CIs.size())));
            inCell.add(cF);
            cF.getNeighbours().forEach(each -> {
                if (!inCell.contains(each) && !frontierCell.contains(each))
                    frontierCell.add(each);
            });
            removeWall(cF, cI);
            frontierCell.remove(cF);
        }

        if (core.isDraw()) System.out.println("Minimal tiles to go " + solve());
        else solve();
    }

    private int solve() {
        while (!Tile.unvistitedTiles.isEmpty()) {
            Tile current = getTileWithShortestDistance();
            current.getOpenNeighbours().stream().filter(each -> Tile.unvistitedTiles.contains(each)).forEach(each -> {
                if (each.getDistance() > current.getDistance() + 1) {
                    each.setDistance(current.getDistance() + 1);
                    each.setLast(current);
                }
            });
            current.vistited();
        }
        Tile.vistitedTiles.sort((o1, other) -> {
            if (o1.getDistance() > other.getDistance()) return 1;
            else if (o1.getDistance() < other.getDistance()) return -1;
            return 0;
        });
        return minTiles = entrance.getDistance();
    }

    private Tile getRandomTile() {
        return getTile(PApplet.floor(core.random(Core.tiles)), PApplet.floor(core.random(Core.tiles)));
    }

    private Tile getTileWithShortestDistance() {
        Tile.unvistitedTiles.sort((o1, other) -> {
            if (o1.getDistance() > other.getDistance()) return 1;
            else if (o1.getDistance() < other.getDistance()) return -1;
            return 0;
        });
        return Tile.unvistitedTiles.get(0);
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x > Core.tiles - 1 || y > Core.tiles - 1)
            return null;
        else return tiles.get(y + Core.tiles * x);
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Tile getEntrance() {
        return entrance;
    }

    public Tile getExit() {
        return exit;
    }

    public Core getCore() {
        return core;
    }

    public ArrayList<Tile> getFrontierCell() {
        return frontierCell;
    }

    public ArrayList<Tile> getInCell() {
        return inCell;
    }

    public int minimalTilesToGo(){
        return minTiles;
    }
}
