package it.naddeil.ro.common.api;

import java.util.List;


/**
 * Problema in forma standard del tipo:
 * min c^T x
 * Ax = b
 * che dato n = numero di righe ha come ultime n variabili una base ammissibile
 */
public class PublicStdFormProblem {
    private List<List<Double>> a;
    private List<Double> b;
    private List<Double> c;
	public List<List<Double>> getA() {
		return a;
	}
	public void setA(List<List<Double>> a) {
		this.a = a;
	}
	public List<Double> getB() {
		return b;
	}
	public void setB(List<Double> b) {
		this.b = b;
	}
	public List<Double> getC() {
		return c;
	}
	public void setC(List<Double> c) {
		this.c = c;
	}
}
