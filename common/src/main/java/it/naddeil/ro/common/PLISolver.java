package it.naddeil.ro.common;

import it.naddeil.ro.common.pub.PublicProblem;

public interface PLISolver {
    public Result solve(PublicProblem problem, Parameters parameters);
}
