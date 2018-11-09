package ActivationFunctions;

public class HyperbolicTagent implements ActivationFunction {

    @Override
    public float activation(float f) {
        double epx = Math.pow(Math.E, f);
        double enx = Math.pow(Math.E, -f);

        return (float) ((epx - enx) / (epx + enx));
    }

    @Override
    public float derivative(float f) {
        float tan = activation(f);
        return 1 - tan * tan;
    }
}
