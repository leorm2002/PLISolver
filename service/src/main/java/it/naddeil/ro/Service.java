package it.naddeil.ro;


import java.util.List;

import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.gomorysolver.GomorySolver;
import it.naddeil.ro.simplexsolver.SimplexSolver;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class Service {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinearDual")
    public List<Double> solveL(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        //Result s = new DualSimplexSolver().solve(problema, new Parameters());
        //SimpleMatrix sol = s.getSoluzione();
        //System.out.println("Tempo impiegato: " + (System.currentTimeMillis() - startTime) + "ms");
        //return IntStream.range(0, sol.getNumElements()).mapToObj(sol::get).toList();
        return null;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinearSimplex")
    public Fraction[][] solveLS(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        FracResult s = new SimplexSolver().solve(problema);
        System.out.println("Tempo impiegato: " + (System.currentTimeMillis() - startTime) + "ms");

        return s.getTableau();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinearInteger")
    public String solveLI(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        GomorySolver gomory = new GomorySolver(null,new DualSimplexSolver());
        FracResult r = gomory.solve(problema, new Parameters());
        System.out.println(r.getSoluzione());
        System.out.println("Tempo impiegato: " + (System.currentTimeMillis() - startTime) + "ms");
        return "";
    }
}

