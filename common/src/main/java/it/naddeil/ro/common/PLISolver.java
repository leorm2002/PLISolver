package it.naddeil.ro.common;

import it.naddeil.ro.common.pub.PublicProblem;

public interface PLISolver {
    public FracResult solve(PublicProblem problem, Parameters parameters);
}
