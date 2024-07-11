package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Fraction;


public class SimplexSolver {
    public FracResult solve(PublicProblem problem) {
        // Il metodo due fasi prende in input un tableau della forma
        // min c^T x
        // Ax = b
        Fraction[][] tableau =  Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problem)).toTableauFormProblem();
        Fraction[][] result = DueFasi.applicaMetodoDueFasi(tableau);
        return FracResult.fromTableau(result);
    }
}
