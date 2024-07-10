package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.Result;
import java.util.Collections;
import java.util.Arrays;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.StdProblem;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.FracResult;
import it.naddeil.ro.common.Fraction;


public class DualSimplexSolver {

    public Result solve(PublicProblem problema, Parameters parameters) {

        StdProblem problem = Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problema)).toMatrixProblem();
        problem.setC(problem.getC().negative());
        SimplessoDuale dualSimplex = SimplessoDuale.createFromStd(problem.getA(), problem.getB(), problem.getC(), Collections.emptyList());
        DualSimplexMessageBuilder msg = new DualSimplexMessageBuilder();
        SimpleMatrix sol = dualSimplex.solve(msg);

        return dualSimplex.buildResult(sol);
    }

    public Result reOptimize(Problema matrixProblem, Parameters parameters) {
        //Aggiungi due righe che formano base
        SimplessoDuale dualSimplex = SimplessoDuale.createFromStd(matrixProblem.getA(), matrixProblem.getB(), matrixProblem.getC(), matrixProblem.getBasis());
        DualSimplexMessageBuilder messageBuilder = new DualSimplexMessageBuilder();
        SimpleMatrix solution = dualSimplex.solve(messageBuilder);

        return dualSimplex.buildResult(solution);
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
