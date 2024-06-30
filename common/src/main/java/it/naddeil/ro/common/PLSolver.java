package it.naddeil.ro.common;

import it.naddeil.ro.common.pub.PublicProblem;

public interface PLSolver {

    public Result solve(PublicProblem problem, Parameters parameters);


    public Result reOptimize(Problema problem, Parameters parameters);
}
