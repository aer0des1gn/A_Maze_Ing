package NeuroNetwork;

public class InputNeuron extends Neuron {

    private float value = 0f;

    @Override
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
