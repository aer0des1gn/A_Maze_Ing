package NeuroNetwork;

public class Connection {

    private float weight;
    private float momentum;
    private Neuron neuron;

    Connection(Neuron neuron, float weight) {
        this.neuron = neuron;
        this.weight = weight;
    }

    public float getValue() {
        return weight * neuron.getValue();
    }

    public void addWeight(float weightDelta) {
        momentum += weight;
        momentum *= 0.9f;
        weight += weightDelta + momentum;
    }

    public float getWeight() {
        return weight;
    }

    public Neuron getNeuron() {
        return neuron;
    }
}
