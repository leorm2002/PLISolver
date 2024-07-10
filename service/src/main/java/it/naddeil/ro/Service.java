package it.naddeil.ro;


import java.util.stream.IntStream;
import java.util.Collections;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.Result;
import it.naddeil.ro.common.StdProblem;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.pub.PublicStdFormProblem;
import it.naddeil.ro.dualsimplexsolver.DualSimplexMessageBuilder;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.dualsimplexsolver.SimplessoDuale;
import it.naddeil.ro.gomorysolver.GomorySolver;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class Service {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinear")
    public List<Double> solveL(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        Result s = new DualSimplexSolver().solve(problema, new Parameters());
        SimpleMatrix sol = s.getSoluzione();
        System.out.println("Tempo impiegato: " + (System.currentTimeMillis() - startTime) + "ms");
        return IntStream.range(0, sol.getNumElements()).mapToObj(sol::get).toList();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinearInteger")
    public String solveLI(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        GomorySolver gomory = new GomorySolver(new DualSimplexSolver());
        Result r = gomory.solve(problema, new Parameters());
        System.out.println(r.getSoluzione());
        System.out.println("Tempo impiegato: " + (System.currentTimeMillis() - startTime) + "ms");
        return "";
    }
}

