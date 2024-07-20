package it.naddeil.ro.dualsimplexsolver;

import java.util.Arrays;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Value;

public class DualSimplexSolver {

    public FracResult solve(PublicProblem problema, Parameters parameters) {

        Value[][] problem = Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problema)).toFractionTableauFormProblem();
        // Nego la funzione obbiettivo
        problem[0] = Arrays.stream(problem[0]).map(Value::negate).toArray(Value[]::new);
        // SimplessoDuale dualSimplex = SimplessoDuale.createFromStd(problem.getA(), problem.getB(), problem.getC(), Collections.emptyList());
        // DualSimplexMessageBuilder msg = new DualSimplexMessageBuilder();
        // SimpleMatrix sol = dualSimplex.solve(msg);

        // return dualSimplex.buildResult(sol);

        return null;
    }

    public FracResult riottimizza(Value[][] tableau) {
        SimplessoDualeFrazionario dualSimplex = new SimplessoDualeFrazionario(tableau);
        dualSimplex.solve();
        return FracResult.fromTableau(dualSimplex.getTableau()).setOut(dualSimplex.getPassaggi());
    }

}
