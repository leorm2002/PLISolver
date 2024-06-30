package it.naddeil.ro.common;

import org.ejml.simple.SimpleMatrix;

/**
 * Problema in forma standard del tipo:
 * min c^T x
 * Ax = b
 * che dato n = numero di righe ha come ultime n variabili una base ammissibile
 */
public class StdProblem {

    SimpleMatrix A;
    SimpleMatrix b;
    SimpleMatrix c;
	public StdProblem() {
	}
    public StdProblem(StdProblemPublic p) {
        	A = new SimpleMatrix(p.getA().size(), p.getA().get(0).size());
        	for(int i = 0; i < p.getA().size(); i++) {
        		for(int j = 0; j < p.getA().get(0).size(); j++) {
        			A.set(i, j, p.getA().get(i).get(j));
        		}
        	}
        	b = new SimpleMatrix(p.getB().size(), 1);
        	for(int i = 0; i < p.getB().size(); i++) {
        		b.set(i, 0, p.getB().get(i));
        	}
        	c = new SimpleMatrix(p.getC().size(), 1);
        	for(int i = 0; i < p.getC().size(); i++) {
        		c.set(i, 0, p.getC().get(i));
        	}
	}

	public StdProblem(SimpleMatrix a, SimpleMatrix b, SimpleMatrix c) {
		A = a;
		this.b = b;
		this.c = c;
	}
	public SimpleMatrix getA() {
		return A;
	}
	public void setA(SimpleMatrix a) {
		A = a;
	}
	public SimpleMatrix getB() {
		return b;
	}
	public void setB(SimpleMatrix b) {
		this.b = b;
	}
	public SimpleMatrix getC() {
		return c;
	}
	public void setC(SimpleMatrix c) {
		this.c = c;
	}
    
}
