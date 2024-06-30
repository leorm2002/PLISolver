package it.naddeil.ro;


import java.util.stream.IntStream;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Problema;
import it.naddeil.ro.common.StdProblem;
import it.naddeil.ro.common.StdProblemPublic;
import it.naddeil.ro.dualsimplexsolver.SimplessoDuale;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/solveLinear")
    public String solve(Problema problem){
        return "Solving the problem";
    }

    @POST
    @Path("/solveStdProblemLinear")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Double> solve(StdProblemPublic p){
        StdProblem problem = new StdProblem(p);
        SimplessoDuale dualSimplex = SimplessoDuale.createFromTableau(problem.getA(), problem.getB(), problem.getC().transpose());
        SimpleMatrix sol = dualSimplex.solve();
        return IntStream.range(0, sol.getNumElements()).mapToObj(sol::get).toList();
    }
}

