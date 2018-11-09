package NeuroNetwork;

import ActivationFunctions.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class WorkingNeuron extends Neuron {

    private List<Connection> connections = new ArrayList<>();
    private ActivationFunction function = ActivationFunction.Sigmoid;
    private float smallDelta = 0;
    private float value = 0;
    private boolean isValueCalc = false;

    public float getValue() {

//        if (!isValueCalc) {
        float sum = 0f;

        for (Connection connection : connections)
            sum += connection.getValue();

        return function.activation(sum);
//        isValueCalc = true;
//        }

//        return value;

    }

    public void addConnection(Neuron neuron, float weight) {
        connections.add(new Connection(neuron, weight));
    }

    public void deltaLearing(float epsilon) {

        float mul = epsilon * smallDelta * function.derivative(getValue());

        for (Connection connection : connections) {
            float bigDelta = mul * connection.getNeuron().getValue();
            connection.addWeight(bigDelta);
        }
    }

    public void calcOutputDelta(float should) {
        smallDelta = should - getValue();
    }

    public void backpropagateSmallDelta() {
        for (Connection c : connections) {
            if (c.getNeuron() instanceof WorkingNeuron) {
                WorkingNeuron wn = (WorkingNeuron) c.getNeuron();
                wn.smallDelta += smallDelta * c.getWeight();
            }
        }
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void resetSmallDelta() {
        isValueCalc = false;
        smallDelta = 0;
    }
}