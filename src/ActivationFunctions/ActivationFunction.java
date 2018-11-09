package ActivationFunctions;

public interface ActivationFunction {

    ActivationFunction Identity = new Identity();
    ActivationFunction Sigmoid = new Sigmoid();
    ActivationFunction Boolean = new Boolean();
    ActivationFunction HyperbolicTangent = new Boolean();

    float activation(float f);

    float derivative(float f);

}
