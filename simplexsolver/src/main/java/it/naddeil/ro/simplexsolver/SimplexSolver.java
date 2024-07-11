package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.FracResult;
import it.naddeil.ro.common.Fraction;


public class SimplexSolver {


    public FracResult solve(PublicProblem problem) {
        // Il metodo due fasi prende in input un tableau della forma
        // min c^T x
        // Ax = b
        
        Fraction[][] tableau =  Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problem)).toMatrixProblem().toTableau();
        Fraction[][] result = DueFasi.applicaMetodoDueFasi(tableau);
        return FracResult.fromTableau(result);
    }
}
