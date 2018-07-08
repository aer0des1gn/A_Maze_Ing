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
        if (!exists)
            System.out.println("Removing wall at " + a.toString() + " - " + b.toString());
    }
}
