package it.naddeil.ro;


import java.util.stream.IntStream;
import java.util.Collections;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.StdProblem;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.pub.PublicStdFormProblem;
import it.naddeil.ro.dualsimplexsolver.SimplessoDuale;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class Service {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinear")
    public String solveL(PublicProblem problem){
        return "Solving the problem";
    }

    @POST
    @Path("/solveStdProblemLinear")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Double> solveLStd(PublicStdFormProblem p){
        StdProblem problem = new StdProblem(p);
        SimplessoDuale dualSimplex = SimplessoDuale.createFromTableau(problem.getA(), problem.getB(), problem.getC().transpose(), Collections.emptyList());
        SimpleMatrix sol = dualSimplex.solve();
        return IntStream.range(0, sol.getNumElements()).mapToObj(sol::get).toList();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinearInteger")
    public String solveLI(PublicProblem problem){
        return "Solving the problem";
    }


    @POST
    @Path("/solveStdProblemLinearInteger")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Double> solveLIStd(PublicStdFormProblem p){
        StdProblem problem = new StdProblem(p);
        SimplessoDuale dualSimplex = SimplessoDuale.createFromTableau(problem.getA(), problem.getB(), problem.getC().transpose(), Collections.emptyList());
        SimpleMatrix sol = dualSimplex.solve();
        return IntStream.range(0, sol.getNumElements()).mapToObj(sol::get).toList();
    }

}

