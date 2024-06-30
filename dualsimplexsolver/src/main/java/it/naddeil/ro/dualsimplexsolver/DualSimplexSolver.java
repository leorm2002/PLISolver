package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.api.PLSolver;
import it.naddeil.ro.api.Parameters;
import it.naddeil.ro.api.Problem;
import it.naddeil.ro.api.Result;

public class DualSimplexSolver implements PLSolver {

    public DualSimplexSolver() {
    }

    // Prende in input un problema in forma standard
    // Lo converte nel duale

    @Override
    public Result solve(Problem problem, Parameters parameters) {

        // Algoritmo del simplesso duale
        // 1. Cotruiamo il duale
        // 2. Scelta della Variabile da Uscire dalla Base (scegliamo x_b negativa)
        // 3. Scelta della Variabile da Entrare nella Base
        // Calcoliamo i costi ridotti

        throw new UnsupportedOperationException("Unimplemented method 'solve'");
    }
}
