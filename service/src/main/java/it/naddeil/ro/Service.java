package it.naddeil.ro;


import java.util.List;

import it.naddeil.ro.apachesimplexsolver.MySimplexSolver;
import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Response;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.gomorysolver.GomorySolver;
import it.naddeil.ro.simplexsolver.ConcreteSimplexSolver;
import jakarta.ws.rs.GET;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/solveLinearSimplex")
    public Response solveLS(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        FracResult s = new ConcreteSimplexSolver().solve(problema);
        long time = System.currentTimeMillis() - startTime;
        return convert(s, time);
    }

    Response convert(FracResult r, long time){
        Response res = new Response();
        res.tableau = r.getTableauStr();
        res.time = time;
        res.soluzione = r.getValoreVariabili().stream().map(Fraction::toString).toList();
        return res;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/solveLinearInteger")
    public Response solveLI(PublicProblem problema){
        long startTime = System.currentTimeMillis();
        FracResult r = new GomorySolver(new MySimplexSolver(),new DualSimplexSolver()).solve(problema, new Parameters());
        
        FracResult s = FracResult.fromTableau(r.getTableau());
        long time = System.currentTimeMillis() - startTime;
        return convert(r, time);
    }

}

