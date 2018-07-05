import processing.core.PApplet;

public class Core extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Core", args);
    }

    public void settings() {
        size(600,600);
    }

    public void setup() {
        frameRate(30);
    }

    public void draw() {
        surface.setTitle("A-Maze-Ing, FPS: " + round(frameRate));
    }
}