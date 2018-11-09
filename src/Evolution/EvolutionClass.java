package Evolution;

import AMazeING.Core;
import AMazeING.Maze;
import AMazeING.Tile;
import NeuroNetwork.InputNeuron;
import NeuroNetwork.WorkingNeuron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EvolutionClass {

    private ArrayList<Agent> agents = new ArrayList<>();

    private ArrayList<Agent> bestAgents = new ArrayList<>();

    private int step;

    private Core core;

    public EvolutionClass(Core core) {
        this.core = core;
        findResetGenerate();
    }

    public void doStep() {

        if (core.getActualMaze().getEntrance().getDistance() * 2 < step++) {
            findResetGenerate();
        }

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

            int use = 1;

            List<WorkingNeuron> outputNeurons = agent.getNeuronalNetwork().getOutputNeurons();
            for (int i = 0; i < outputNeurons.size(); i++) {
                if (outputNeurons.get(use).getValue() < outputNeurons.get(i).getValue())
                    use = i;
            }

            Tile nTile = null;

            switch (use) {
                case 0:
                    nTile = !agent.getCurrentPosition().isNorth() ? agent.getCurrentPosition().getNorth() : null;
                    break;
                case 1:
                    nTile = !agent.getCurrentPosition().isEast() ? agent.getCurrentPosition().getEast() : null;
                    break;
                case 2:
                    nTile = !agent.getCurrentPosition().isSouth() ? agent.getCurrentPosition().getSouth() : null;
                    break;
                case 3:
                    nTile = !agent.getCurrentPosition().isWest() ? agent.getCurrentPosition().getWest() : null;
                    break;
            }

            if (nTile != null)
                agent.setCurrentPosition(nTile);

        });

    }

    private void generateNewGeneration(Maze maze) {
        if (bestAgents.isEmpty()) {
            int i = 0;
            while (i++ < 50) {
                Agent agent = new Agent(1);
                agent.setCurrentPosition(maze.getEntrance());
                agent.init();
                agents.add(agent);
            }
        } else {
            Iterator<Agent> iterator = bestAgents.iterator();
            Agent agent = iterator.next();
            int i = 0;
            while (true) {
                if (i++ % 10 == 0) {
                    Agent copy = new Agent(agent.getGeneration());
                    copy.setCurrentPosition(maze.getEntrance());
                    copy.init(agent.getWeights());
                    agents.add(copy);
                    if (iterator.hasNext()) {
                        agent = iterator.next();
                    } else
                        return;
                } else {
                    Agent temp = new Agent(agent.getGeneration() + 1);
                    temp.setCurrentPosition(maze.getEntrance());
                    temp.init(changeWeights(agent.getWeights()));
                    agents.add(temp);
                }
            }
        }
    }

    private float[] changeWeights(float[] weights) {
        for (int i = 0; i < weights.length / 20; i++)
            weights[(int) core.random(weights.length)] += core.random(-.5f, .5f);
        return weights;
    }

    private void findBestAgents() {
        bestAgents.clear();
        agents.stream()
                .filter(agent -> agent.getCurrentPosition().getDistance() <= core.getActualMaze().getEntrance().getDistance() - 8)
                .sorted((o1, o2) -> {
                    int dist1 = o1.getCurrentPosition().getDistance();
                    int dist2 = o2.getCurrentPosition().getDistance();
                    return Integer.compare(dist1, dist2);
                })
                .forEachOrdered(agent -> {
                    if (bestAgents.size() < 5) {
                        if (Core.debug)
                            System.out.println("#" + bestAgents.size() + ": " + agent.getCurrentPosition().getDistance());
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
