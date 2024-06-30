package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.Result;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.MatrixProblem;
import it.naddeil.ro.common.PLSolver;


public class DualSimplexSolver implements PLSolver{

    @Override
    public Result solve(Problema problem, Parameters parameters) {
        MatrixProblem matrixProblem = problem.toMatrixProblem();
        SimplessoDuale dualSimplex = SimplessoDuale.createFromCanonical(matrixProblem.getA(), matrixProblem.getB(), matrixProblem.getC());
        SimpleMatrix solution = dualSimplex.solve();

        return new DualSimplexResult(null, solution);

        

    }

    @Override
    public Result reOptimize(Problema problem, Parameters parameters, Result result) {
        MatrixProblem matrixProblem = problem.toMatrixProblem();
        SimplessoDuale dualSimplex = SimplessoDuale.createFromTableau(matrixProblem.getA(), matrixProblem.getB(), matrixProblem.getC());
        SimpleMatrix solution = dualSimplex.solve();

        return new DualSimplexResult(null, solution);
    }
    
}
