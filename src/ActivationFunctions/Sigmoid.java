package ActivationFunctions;

public class Sigmoid implements ActivationFunction {

    @Override
    public float activation(float f) {
        return (float) (1f / (1f + Math.pow(Math.E, -f)));
    }

    @Override
    public float derivative(float f) {
        float sigmoid = activation(f);
        return sigmoid * (1f - sigmoid);
    }
}
