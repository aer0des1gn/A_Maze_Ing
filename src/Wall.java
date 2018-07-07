import java.util.ArrayList;

public class Wall {

    private Tile a;
    private Tile b;

    private boolean existing = true;

    Wall(Tile a, Tile b) {
        if (a == null || b == null)
            return;
        System.out.println("Creating wall at " + a.toString() + " - " + b.toString());
        this.a = a;
        this.b = b;
    }

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean exists) {
        this.existing = exists;
    }

    public Tile getOpposite(Tile t) {
        if (t == a) return b;
        else if (t == b) return a;
        else return null;
    }

    public ArrayList<Tile> getTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();
        if(a != null)
        tiles.add(a);
        if(b != null)
        tiles.add(b);
        return tiles;
    }
}
