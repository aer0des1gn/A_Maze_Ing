package ActivationFunctions;

public class Identity implements ActivationFunction {

    @Override
    public float activation(float f) {
        return f;
    }

    @Override
    public float derivative(float f) {
        return 1;
    }
}
