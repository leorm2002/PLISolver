package it.naddeil.ro.common;

public interface PLSolver {

    public Result solve(Problema problem, Parameters parameters);


    public Result reOptimize(Problema problem, Parameters parameters, Result result);
}
