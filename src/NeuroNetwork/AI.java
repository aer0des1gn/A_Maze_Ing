package NeuroNetwork;

import AMazeING.Core;
import AMazeING.Maze;
import AMazeING.Tile;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    public static int generation = 1;
    public NeuronalNetwork nn = new NeuronalNetwork();

    private Tile currentPosition;
    private int gen = generation;

    private float points = 0;
    private ArrayList<Tile> visited = new ArrayList<>();
    private boolean ended = false;

    public Tile getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Tile currentPosition) {
        points -= 0.04;
        if (currentPosition == null) {
            points -= 0.75;
            isEnded();
            return;
        }
        if (this.currentPosition.getLastTile().equals(currentPosition))
            points += .5;
        else
            points -= .5;
        if (!visited.contains(currentPosition)) {
            visited.add(currentPosition);
        } else {
            points -= 0.25;
        }
        if (currentPosition.getDistance() == 0) {
            points += 1;
            ended = true;
        }
        isEnded();
        this.currentPosition = currentPosition;
    }

    public void setStart(Maze maze) {
        this.currentPosition = maze.getEntrance();
    }

    public void init() {

        for (int i = 0; i < 4; i++) {
            nn.createNewOutput();
        }

        nn.createHiddenNeurons(20);

        for (int i = 0; i < 6; i++) {
            nn.createNewInput();
        }

        Random r = new Random();
        float[] weights = new float[nn.getOutputNeurons().size() * nn.getHiddenNeurons().size() + nn.getInputNeurons().size() * nn.getHiddenNeurons().size()];
        for (int i = 0; i < weights.length; i++)
            weights[i] = r.nextFloat();

        nn.createFullMesh(weights);
    }

    public void init(float[] weights) {
        for (int i = 0; i < 4; i++) {
            nn.createNewOutput();
        }

        nn.createHiddenNeurons(20);

        for (int i = 0; i < 6; i++) {
            nn.createNewInput();
        }

        nn.createFullMesh(weights);
    }

    public boolean isEnded() {
        if (!ended)
            ended = points <= -.5 * Core.tiles;
        return ended;
    }

    public float getPoints() {
        return points;
    }
}
