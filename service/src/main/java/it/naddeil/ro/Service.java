package it.naddeil.ro;

import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Response;
import it.naddeil.ro.common.exceptions.BaseException;
import it.naddeil.ro.common.models.Result;
import it.naddeil.ro.common.utils.Value;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.gomorysolver.GomorySolver;
import it.naddeil.ro.simplexsolver.ConcreteSimplexSolver;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class Service {
    Response convert(Result r, long time) {
        Response res = new Response();
        res.tableau = r.getTableauStr();
        res.time = time;
        res.soluzione = r.getValoreVariabili().stream().map(Value::toString).toList();
        res.passiRisoluzione = r.getOut();
        return res;
    }

    Response convert(BaseException e, long time) {
        Response res = new Response();
        res.time = time;
        res.passiRisoluzione = e.getState();
        res.error = e.getMessage();
        return res;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/solveLinearSimplex")
    public Response solveLS(PublicProblem problema) {
        long startTime = System.currentTimeMillis();

        try {
            Result s = new ConcreteSimplexSolver().solve(problema);
            long time = System.currentTimeMillis() - startTime;
            return convert(s, time);
        } catch (BaseException e) {
            long time = System.currentTimeMillis() - startTime;
            return convert(e, time);
        } catch (Throwable e) {
            throw e;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/solveLinearInteger")
    public Response solveLI(PublicProblem problema) {
        long startTime = System.currentTimeMillis();
        try {
            Result r = new GomorySolver(new ConcreteSimplexSolver(), new DualSimplexSolver()).solve(problema);
            long time = System.currentTimeMillis() - startTime;
            return convert(r, time);
        } catch (BaseException e) {
            long time = System.currentTimeMillis() - startTime;
            return convert(e, time);
        } catch (Throwable e) {
            throw e;
        }
    }
}
