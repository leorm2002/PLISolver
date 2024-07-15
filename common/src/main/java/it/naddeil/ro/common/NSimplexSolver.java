package it.naddeil.ro.common;

import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;

public interface NSimplexSolver {

    FracResult solve(PublicProblem problem);

}