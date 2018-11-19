package Evolution;

import AMazeING.Core;
import AMazeING.Maze;
import AMazeING.Tile;
import NeuroNetwork.InputNeuron;
import NeuroNetwork.WorkingNeuron;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EvolutionClass {

    private ArrayList<Agent> agents = new ArrayList<>();

    private ArrayList<Agent> bestAgents = new ArrayList<>();

    private int step;

    private Core core;

    //CONFIGURE

    private int lastBestAgents = 1;
    private int randomClones = 500;
    private float changeWeightsPerWeight = .5f;
    private int changeWeightsInProcent = 10;

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

        boolean finished = false;
        for (Agent agent : agents) {

            if (finished)
                break;

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
                        inputNeuron.setValue(agent.visitedCount());
                        break;
                    case 6:
                        inputNeuron.setValue(agent.getLastOutput());
                        break;
                    case 7:
                        inputNeuron.setValue(agent.getPunish());
                        break;
                    //bias
                    case 8:
                        inputNeuron.setValue(-3f);
                        break;
                }
            }


            List<WorkingNeuron> outputNeurons = agent.getNeuronalNetwork().getOutputNeurons();
            agent.setLastOutput(outputNeurons.get(outputNeurons.size() - 1).getValue());

            HashMap<Integer, Float> neuronFloatHashMap = new HashMap<>();

            for (int i = 0; i < outputNeurons.size() - 1; i++) {
                neuronFloatHashMap.put(i, outputNeurons.get(i).getValue());
            }

            neuronFloatHashMap = neuronFloatHashMap.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            for (Integer i : neuronFloatHashMap.keySet()) {
                Tile nTile = getNTile(i, agent.getCurrentPosition());
                if (nTile != null) {
                    agent.setCurrentPosition(nTile);
                    agent.addPunish(Math.min(agent.visitedCount(), 10) * .1f);
                    if (nTile.getDistance() == 0) {
                        System.out.println("Finished!");
                        System.out.println("Found perfect weights for this maze!");
                        System.out.println("Within " + agent.getGeneration() + " generations");
                        finished = true;
                        core.getNextMaze();
                    }
                    break;
                } else {
                    agent.addPunish(0.15f);
                }
            }
        }
        if (finished || core.getActualMaze().getEntrance().getDistance() * 2 < step++)
            findResetGenerate();

    }

    private void generateNewGeneration(Maze maze) {
        Iterator<Agent> iterator = bestAgents.iterator();
        Agent agent;
        while (iterator.hasNext()) {
            agent = iterator.next();
            int i = 1;
            while (i++ % randomClones != 0) {
                Agent temp = new Agent(agent.getGeneration() + 1);
                temp.setCurrentPosition(maze.getEntrance());
                temp.init(changeWeights(agent.getWeights()));
                agents.add(temp);
            }
            Agent copy = new Agent(agent.getGeneration());
            copy.setCurrentPosition(maze.getEntrance());
            copy.init(agent.getWeights());
            agents.add(copy);
        }
        while (agents.size() < randomClones * lastBestAgents + 20) {
            agent = new Agent(1);
            agent.setCurrentPosition(maze.getEntrance());
            agent.init();
            agents.add(agent);
        }
    }

    private float[] changeWeights(float[] weights) {
        for (int i = 0; i < weights.length / 100 * changeWeightsInProcent; i++)
            weights[(int) core.random(weights.length)] += core.random(-changeWeightsPerWeight, changeWeightsPerWeight);
        return weights;
    }

    private void findBestAgents() {
        bestAgents.clear();
        agents.stream()
                .sorted(Comparator.comparing(o -> o.getCurrentPosition().getDistance() + o.getPunish()))
                .forEachOrdered(agent -> {
                    if (bestAgents.size() < lastBestAgents) {
                        bestAgents.add(agent);
                        if (Core.debug) {
                            System.out.println("dist: " + agent.getCurrentPosition().getDistance());
                            System.out.println("punish: " + agent.getPunish());
                            System.out.println("#" + bestAgents.size() + ": " + (agent.getCurrentPosition().getDistance() + agent.getPunish()));
                        }
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
        AtomicInteger i = new AtomicInteger(1);
        agents.stream()
                .sorted((o1, o2) -> Integer.compare((int) (o2.getCurrentPosition().getDistance() + o2.getPunish()), (int) (o1.getCurrentPosition().getDistance() + o1.getPunish())))
                .forEachOrdered(agent -> {
                    if (agent.getCurrentPosition() != null) {
                        core.stroke(220 - agents.size() + i.get(), 0, 255);
                        if (i.get() == agents.size())
                            core.fill(220, 0, 255);
                        core.rect(
                                agent.getCurrentPosition().getX() * core.WIDTH + (core.WIDTH / 4),
                                agent.getCurrentPosition().getY() * core.WIDTH + (core.WIDTH / 4),
                                core.WIDTH / 2,
                                core.WIDTH / 2);
                        i.getAndIncrement();
                    }
                });

    }
}
