package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.Result;


import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.PLSolver;


public class GenericSolver implements PLSolver{

    // TODO: Metodo per verificare se la base di partenza è ottima o meno (se non lo è, si può fare fase 1 + fase 2)

    @Override
    public Result solve(PublicProblem problem, Parameters parameters) {
        return null;
    }

    @Override
    public Result reOptimize(Problema matrixProblem, Parameters parameters) {
        return null;
    }
    
}
