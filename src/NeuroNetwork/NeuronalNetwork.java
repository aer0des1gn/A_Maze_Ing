package NeuroNetwork;

import java.util.ArrayList;
import java.util.List;

public class NeuronalNetwork {

    private List<InputNeuron> inputNeurons = new ArrayList<>();
    private List<WorkingNeuron> hiddenNeurons = new ArrayList<>();
    private List<WorkingNeuron> outputNeurons = new ArrayList<>();

    public void createNewInput() {
        inputNeurons.add(new InputNeuron());
    }

    public void createNewOutput() {
        outputNeurons.add(new WorkingNeuron());
    }

    private void reset() {
        outputNeurons.forEach(WorkingNeuron::resetSmallDelta);
        hiddenNeurons.forEach(WorkingNeuron::resetSmallDelta);
    }

    public void createHiddenNeurons(int amount) {
        for (int i = 0; i < amount; i++) {
            hiddenNeurons.add(new WorkingNeuron());
        }
    }

    public List<WorkingNeuron> getHiddenNeurons() {
        return hiddenNeurons;
    }

    public void createFullMesh() {
        if (!hiddenNeurons.isEmpty()) {
            for (WorkingNeuron outputNeuron : outputNeurons) {
                for (WorkingNeuron hiddenNeuron : hiddenNeurons) {
                    outputNeuron.addConnection(hiddenNeuron, 0);
                }
            }
            for (WorkingNeuron hiddenNeuron : hiddenNeurons) {
                for (InputNeuron inputNeuron : inputNeurons) {
                    hiddenNeuron.addConnection(inputNeuron, 0);
                }
            }
        } else {
            for (WorkingNeuron outputNeuron : outputNeurons) {
                for (InputNeuron inputNeuron : inputNeurons) {
                    outputNeuron.addConnection(inputNeuron, 0);
                }
            }
        }
    }

    public void createFullMesh(float... weights) {

        int i = 0;

        if (!hiddenNeurons.isEmpty()) {
            if (weights.length != getOutputNeurons().size() * getHiddenNeurons().size() + getInputNeurons().size() * getHiddenNeurons().size()) {
                throw new RuntimeException();
            }

            for (WorkingNeuron outputNeuron : outputNeurons) {
                for (WorkingNeuron hiddenNeuron : hiddenNeurons) {
                    outputNeuron.addConnection(hiddenNeuron, weights[i++]);
                }
            }
            for (WorkingNeuron hiddenNeuron : hiddenNeurons) {
                for (InputNeuron inputNeuron : inputNeurons) {
                    hiddenNeuron.addConnection(inputNeuron, weights[i++]);
                }
            }
        } else {
            if (weights.length != inputNeurons.size() * outputNeurons.size()) {
                throw new RuntimeException();
            }

            for (WorkingNeuron outputNeuron : outputNeurons) {
                for (InputNeuron inputNeuron : inputNeurons) {
                    outputNeuron.addConnection(inputNeuron, weights[i++]);
                }
            }
        }
    }

    public void backpropagate(float[] shoulds, float epsilon) {
        if (shoulds.length != outputNeurons.size())
            throw new IllegalArgumentException();

        reset();

        for (int i = 0; i < shoulds.length; i++) {
            outputNeurons.get(i).calcOutputDelta(shoulds[i]);
        }

        if (hiddenNeurons.size() > 0) {
            for (int i = 0; i < shoulds.length; i++) {
                outputNeurons.get(i).backpropagateSmallDelta();
            }
        }

        for (int i = 0; i < shoulds.length; i++) {
            outputNeurons.get(i).deltaLearing(epsilon);
        }

        hiddenNeurons.forEach(hiddenNeuron -> hiddenNeuron.deltaLearing(epsilon));

    }

    public float[] getWeights() {

        int i = getOutputNeurons().size() * getHiddenNeurons().size() + getInputNeurons().size() * getHiddenNeurons().size();
        float[] weights = new float[i--];

        for (WorkingNeuron outputNeuron : outputNeurons) {
            for (int j = outputNeuron.getConnections().size() - 1; j >= 0; j--) {
                weights[i--] = outputNeuron.getConnections().get(j).getWeight();
            }
        }
        for (WorkingNeuron hiddenNeuron : hiddenNeurons) {
            for (int j = hiddenNeuron.getConnections().size() - 1; j >= 0; j--) {
                weights[i--] = hiddenNeuron.getConnections().get(j).getWeight();
            }
        }

        return weights;
    }

    public List<InputNeuron> getInputNeurons() {
        return inputNeurons;
    }

    public List<WorkingNeuron> getOutputNeurons() {
        return outputNeurons;
    }
}
