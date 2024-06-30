package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.api.PLSolver;
import it.naddeil.ro.api.Parameters;
import it.naddeil.ro.api.Problem;
import it.naddeil.ro.api.Result;

public class SimplexSolver implements PLSolver {
    private final PLSolver plSolver;

    public SimplexSolver(PLSolver plsolver) {
        this.plSolver = plsolver;

    }

    boolean isSolved(Problem p, Result r) {
        return false;
    }

    Problem aggiungiTaglio(Problem p) {
        // 1. trova riga su cui aggiungere taglio
        int i = 0;
        // 2. calcola taglio

        // 3. esprimi calcolo rispetto a base

        // 4. aggiungi vincolo

        return p;

    }

    @Override
    public Result solve(Problem problem, Parameters parameters) {

        int maxIter = 10;
        for (int i = 0; i < maxIter; i++) {

            final Result r = plSolver.solve(problem, parameters);
            if (isSolved(problem, r)) {
                break;
            }
            problem = aggiungiTaglio(problem);
        }

        // Todo return solution
        return null;
    }

}
