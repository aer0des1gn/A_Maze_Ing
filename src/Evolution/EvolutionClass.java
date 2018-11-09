package Evolution;

import AMazeING.Core;
import AMazeING.Maze;
import AMazeING.Tile;
import NeuroNetwork.InputNeuron;
import NeuroNetwork.WorkingNeuron;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EvolutionClass {

    private ArrayList<Agent> agents = new ArrayList<>();

    private ArrayList<Agent> bestAgents = new ArrayList<>();

    private int step;

    private Core core;

    public EvolutionClass(Core core) {
        this.core = core;
        findResetGenerate();
    }

    private Tile getNTile(int direction, Tile current) {
        switch (direction) {
            case 0:
                return !current.isNorth() ? current.getNorth() : null;
            case 1:
                return !current.isEast() ? current.getEast() : null;
            case 2:
                return !current.isSouth() ? current.getSouth() : null;
            case 3:
                return !current.isWest() ? current.getWest() : null;
        }
        return null;
    }

    public void doStep() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        agents.forEach(agent -> {

            for (int i = 0; i < agent.getNeuronalNetwork().getInputNeurons().size(); i++) {

                InputNeuron inputNeuron = agent.getNeuronalNetwork().getInputNeurons().get(i);

                switch (i) {
                    case 0:
                        inputNeuron.setValue(agent.getCurrentPosition().isNorth() ? 1 : 0);
                        break;
                    case 1:
                        inputNeuron.setValue(agent.getCurrentPosition().isEast() ? 1 : 0);
                        break;
                    case 2:
                        inputNeuron.setValue(agent.getCurrentPosition().isSouth() ? 1 : 0);
                        break;
                    case 3:
                        inputNeuron.setValue(agent.getCurrentPosition().isWest() ? 1 : 0);
                        break;
                    case 4:
                        inputNeuron.setValue(agent.getCurrentPosition().getDistance());
                        break;
                    case 5:
                        inputNeuron.setValue(agent.hasVisited() ? 1 : 0);
                        break;
                }
            }

            int use = 0;

            List<WorkingNeuron> outputNeurons = agent.getNeuronalNetwork().getOutputNeurons();
            for (int i = 0; i < outputNeurons.size(); i++) {
                if (outputNeurons.get(use).getValue() < outputNeurons.get(i).getValue())
                    use = i;
            }

            Tile nTile = getNTile(use, agent.getCurrentPosition());
            if (nTile != null) {
                atomicBoolean.set(true);
                agent.setCurrentPosition(nTile);
                if (nTile.getDistance() == 0) {
                    System.out.println("Finished!");
                    System.out.println("Found perfect weights for this maze!");
                    System.out.println("Within " + agent.getGeneration() + " generations");
                    System.out.println(Arrays.toString(agent.getWeights()));
                    core.getNextMaze();
                }
            }
        });

        if (!atomicBoolean.get() || core.getActualMaze().getEntrance().getDistance() * 4 < step++) {
            findResetGenerate();
        }

    }

    private void generateNewGeneration(Maze maze) {
        Iterator<Agent> iterator = bestAgents.iterator();
        Agent agent;
        if (iterator.hasNext()) {
            agent = iterator.next();
            int i = 0;
            while (true) {
                if (i++ % 5 == 0) {
                    Agent copy = new Agent(agent.getGeneration());
                    copy.setCurrentPosition(maze.getEntrance());
                    copy.init(agent.getWeights());
                    agents.add(copy);
                    if (iterator.hasNext()) {
                        agent = iterator.next();
                    } else
                        break;
                } else {
                    Agent temp = new Agent(agent.getGeneration() + 1);
                    temp.setCurrentPosition(maze.getEntrance());
                    temp.init(changeWeights(agent.getWeights()));
                    agents.add(temp);
                }
            }
        }
        while (agents.size() < 30) {
            agent = new Agent(1);
            agent.setCurrentPosition(maze.getEntrance());
            agent.init();
            agents.add(agent);
        }
    }

    private float[] changeWeights(float[] weights) {
        for (int i = 0; i < weights.length / 2; i++)
            weights[(int) core.random(weights.length)] += core.random(-.1f, .1f);
        return weights;
    }

    private void findBestAgents() {
        bestAgents.clear();
        agents.stream()
                .filter(agent -> core.getActualMaze().minimalTilesToGo() - agent.getCurrentPosition().getDistance() > core.getActualMaze().minimalTilesToGo() / 4)
                .sorted(Comparator.comparingInt(o -> o.getCurrentPosition().getDistance()))
                .forEachOrdered(agent -> {
                    if (bestAgents.size() < 10) {
                        if (Core.debug) {
                            System.out.println("#" + bestAgents.size() + ": " + agent.getCurrentPosition().getDistance());
                            System.out.println(Arrays.toString(agent.getWeights()));
                        }
                        bestAgents.add(agent);
                    }
                });
    }

    private void findResetGenerate() {
        findBestAgents();
        agents.clear();
        step = 0;
        generateNewGeneration(core.getActualMaze());
    }

    public void draw() {
        agents.forEach(agent -> {
            if (agent.getCurrentPosition() != null) {
                core.rect(agent.getCurrentPosition().getX() * core.WIDTH + (core.WIDTH / 4),
                        agent.getCurrentPosition().getY() * core.WIDTH + (core.WIDTH / 4), core.WIDTH / 2, core.WIDTH / 2);
            }
        });
    }
}
