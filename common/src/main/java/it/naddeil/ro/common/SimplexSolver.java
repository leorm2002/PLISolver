package it.naddeil.ro.common;

import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.Result;

public interface SimplexSolver {

    Result solve(PublicProblem problem);

}