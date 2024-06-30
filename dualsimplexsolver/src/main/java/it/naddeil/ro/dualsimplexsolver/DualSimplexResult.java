package it.naddeil.ro.dualsimplexsolver;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Result;

public class DualSimplexResult implements Result{
    private SimpleMatrix tableauOttimo;
    private SimpleMatrix soluzione;

    public DualSimplexResult(SimpleMatrix tableauOttimo, SimpleMatrix soluzione) {
        this.tableauOttimo = tableauOttimo;
        this.soluzione = soluzione;
    }

    @Override
    public SimpleMatrix getSoluzione() {
        return soluzione;
    }

    @Override
    public SimpleMatrix getTableauOttimo() {
        return tableauOttimo;
    }
    
}
