package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Fraction;


public class DualSimplexSolver {

    public FracResult solve(PublicProblem problema, Parameters parameters) {

        Fraction[][] problem = Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problema)).toTableauFormProblem();
        //problem.setC(problem.getC().negative());
        //SimplessoDuale dualSimplex = SimplessoDuale.createFromStd(problem.getA(), problem.getB(), problem.getC(), Collections.emptyList());
        //DualSimplexMessageBuilder msg = new DualSimplexMessageBuilder();
        //SimpleMatrix sol = dualSimplex.solve(msg);

        //return dualSimplex.buildResult(sol);
    
        return null;
    }

    public FracResult riottimizza(Fraction[][] tableau){
        SimplessoDualeFrazionario dualSimplex = new SimplessoDualeFrazionario(tableau);
        dualSimplex.solve();
        return FracResult.fromTableau(dualSimplex.getTableau());
    }
    
}
