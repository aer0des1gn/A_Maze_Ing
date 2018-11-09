package ActivationFunctions;

public class Boolean implements ActivationFunction {
    
    @Override
    public float activation(float f) {
        if (f < 0) return 0;
        return 1;
    }

    @Override
    public float derivative(float f) {
        return 1;
    }
}
