package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.StdProblem;


public class DualSimplexSolver {

    public FracResult solve(PublicProblem problema, Parameters parameters) {

        StdProblem problem = Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problema)).toMatrixProblem();
        problem.setC(problem.getC().negative());
        //SimplessoDuale dualSimplex = SimplessoDuale.createFromStd(problem.getA(), problem.getB(), problem.getC(), Collections.emptyList());
        //DualSimplexMessageBuilder msg = new DualSimplexMessageBuilder();
        //SimpleMatrix sol = dualSimplex.solve(msg);

        //return dualSimplex.buildResult(sol);
    
        return null;
    }

    public FracResult riottimizza(Fraction[][] tableau){
        SimplessoDualeFrazionario dualSimplex = new SimplessoDualeFrazionario(tableau);
        // Neghiamo la funzione obbiettivo
        //tableau[0] = Arrays.stream(tableau[0]).map(Fraction::negate).toArray(Fraction[]::new);
        dualSimplex.solve();
        var tableauResult = dualSimplex.getTableau();
        return FracResult.fromTableau(tableauResult);
    }
    
}
