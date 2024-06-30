package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.Result;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.StdProblem;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.PLSolver;


public class DualSimplexSolver implements PLSolver{

    // TODO: Metodo per verificare se la base di partenza è ottima o meno (se non lo è, si può fare fase 1 + fase 2)

    @Override
    public Result solve(PublicProblem problem, Parameters parameters) {
        StdProblem matrixProblem = Problema
            .fromPublic(ProblemTransformer.portaInFormaStandard(problem))
            .toMatrixProblem();

        SimplessoDuale dualSimplex = SimplessoDuale.createFromCanonical(matrixProblem.getA(), matrixProblem.getB(), matrixProblem.getC());
        SimpleMatrix solution = dualSimplex.solve();

        return dualSimplex.buildResult(solution);
    }

    @Override
    public Result reOptimize(Problema matrixProblem, Parameters parameters) {
        SimplessoDuale dualSimplex = SimplessoDuale.createFromTableau(matrixProblem.getA(), matrixProblem.getB(), matrixProblem.getC(), matrixProblem.getBasis());
        SimpleMatrix solution = dualSimplex.solve();

        return dualSimplex.buildResult(solution);
    }
    
}
