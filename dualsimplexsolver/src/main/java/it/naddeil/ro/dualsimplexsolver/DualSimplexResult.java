package it.naddeil.ro.dualsimplexsolver;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Result;


public class DualSimplexResult implements Result {
    private SimpleMatrix tableauOttimo;
    private SimpleMatrix soluzione;
    private  List<Integer> basis;
    private SimpleMatrix cBar;

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

    public void setTableauOttimo(SimpleMatrix tableauOttimo) {
        this.tableauOttimo = tableauOttimo;
    }

    public void setSoluzione(SimpleMatrix soluzione) {
        this.soluzione = soluzione;
    }

    public void setBasis(List<Integer> basis) {
        this.basis = basis;
    }

    public SimpleMatrix getcBar() {
        return cBar;
    }

    public DualSimplexResult setcBar(SimpleMatrix cBar) {
        this.cBar = cBar;
        return this;
    }
    
}
