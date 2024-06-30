package it.naddeil.ro.dualsimplexsolver;

import org.ejml.simple.SimpleMatrix;


public class DualSimplexResult {
    private SimpleMatrix tableauOttimo;
    private SimpleMatrix soluzione;

    public DualSimplexResult(SimpleMatrix tableauOttimo, SimpleMatrix soluzione) {
        this.tableauOttimo = tableauOttimo;
        this.soluzione = soluzione;
    }

    public SimpleMatrix getSoluzione() {
        return soluzione;
    }

    public SimpleMatrix getTableauOttimo() {
        return tableauOttimo;
    }
    
}
