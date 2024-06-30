package it.naddeil.ro.dualsimplexsolver;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Result;


public class DualSimplexResult implements Result {
    private SimpleMatrix tableauOttimo;
    private SimpleMatrix soluzione;
    private  List<Integer> basis;

    public DualSimplexResult(SimpleMatrix tableauOttimo, SimpleMatrix soluzione, List<Integer> basis) {
        this.tableauOttimo = tableauOttimo;
        this.basis = basis;
        this.soluzione = soluzione;
    }

    public SimpleMatrix getSoluzione() {
        return soluzione;
    }

    public SimpleMatrix getTableauOttimo() {
        return tableauOttimo;
    }

	@Override
	public List<Integer> getBasis() {
        return basis;
	}
    
}
