package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;

import java.util.stream.*;
import java.lang.reflect.Array;
import java.util.Arrays;
public class DualSimplexSolver {

    public FracResult solve(PublicProblem problema, Parameters parameters) {

        Comp[][] problem = Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problema)).toTableauFormProblem();
        // Nego la funzione obbiettivo
        problem[0] = Arrays.stream(problem[0]).map(Comp::negate).toArray(Comp[]::new);
        //SimplessoDuale dualSimplex = SimplessoDuale.createFromStd(problem.getA(), problem.getB(), problem.getC(), Collections.emptyList());
        //DualSimplexMessageBuilder msg = new DualSimplexMessageBuilder();
        //SimpleMatrix sol = dualSimplex.solve(msg);

        //return dualSimplex.buildResult(sol);
    
        return null;
    }

    public FracResult riottimizza(Comp[][] tableau){
        SimplessoDualeFrazionario dualSimplex = new SimplessoDualeFrazionario(tableau);
        dualSimplex.solve();
        return FracResult.fromTableau(dualSimplex.getTableau()).setOut(dualSimplex.getPassaggi());
    }
    
}
