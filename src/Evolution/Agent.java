package Evolution;

import AMazeING.Tile;
import NeuroNetwork.Connection;
import NeuroNetwork.NeuronalNetwork;
import NeuroNetwork.WorkingNeuron;

import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private NeuronalNetwork nn;

    private Tile currentPosition;
    private int generation;

    private float lastOutput;

    private float punish = 0;

    private ArrayList<Tile> visited;

    Agent(int generation) {
        this.generation = generation;
        nn = new NeuronalNetwork();
        visited = new ArrayList<>();
    }

    public void addPunish(float punish) {
        if (punish > 0)
            this.punish += punish;
    }

    public float getPunish() {
        return punish;
    }

    public float getLastOutput() {
        return lastOutput;
    }

    public void setLastOutput(float lastOutput) {
        this.lastOutput = lastOutput;
    }

    public Tile getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Tile currentPosition) {
        visited.add(this.currentPosition);
        this.currentPosition = currentPosition;
    }

    public long visitedCount() {
        return visited.stream().filter(tile -> currentPosition.equals(tile)).count();
    }

    private void createLayers() {
        for (int i = 0; i < 5; i++) {
            nn.createNewOutput();
        }

        nn.createHiddenNeurons(10);

        for (int i = 0; i < 9; i++) {
            nn.createNewInput();
        }
    }

    public void init() {

        createLayers();

        Random r = new Random();
        float[] weights = new float[nn.getOutputNeurons().size() * nn.getHiddenNeurons().size() + nn.getInputNeurons().size() * nn.getHiddenNeurons().size()];
        for (int i = 0; i < weights.length; i++)
            weights[i] = r.nextFloat();

        nn.createFullMesh(weights);
    }

    public void init(float[] weights) {

        createLayers();

        nn.createFullMesh(weights);
    }

    public int getGeneration() {
        return generation;
    }

    public float[] getWeights() {

        float[] weights = new float[nn.getOutputNeurons().size() * nn.getHiddenNeurons().size() + nn.getInputNeurons().size() * nn.getHiddenNeurons().size()];
        int i = 0;

        for (WorkingNeuron outputNeuron : nn.getOutputNeurons()) {
            for (Connection connection : outputNeuron.getConnections()) {
                weights[i++] = connection.getWeight();
            }
        }
        for (WorkingNeuron hiddenNeuron : nn.getHiddenNeurons()) {
            for (Connection connection : hiddenNeuron.getConnections()) {
                weights[i++] = connection.getWeight();
            }
        }
        return weights;
    }

    public NeuronalNetwork getNeuronalNetwork() {
        return nn;
    }
}
