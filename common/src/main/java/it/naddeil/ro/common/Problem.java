package it.naddeil.ro.api;

/**
 * Rappresenta un problema di ottimizzazione ovvero min F s.t. Ax=d dove ogni riga della matrice rappresenta un vincolo di tipo =
 */
public interface Problem {
    double[][] getA();

    double[] getD();

    double[] getF();

    void addConstraint(double[] constraint);
}
