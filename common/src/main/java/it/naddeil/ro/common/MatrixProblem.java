package it.naddeil.ro.common;

import org.ejml.simple.SimpleMatrix;

public interface MatrixProblem {
    SimpleMatrix getA();
    void setA(SimpleMatrix a);
    SimpleMatrix getB();
    void setB(SimpleMatrix b);
    SimpleMatrix getC();
    void setC(SimpleMatrix c);

    MatrixProblem copy();
}
