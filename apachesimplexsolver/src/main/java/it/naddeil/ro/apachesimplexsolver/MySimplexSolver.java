package it.naddeil.ro.apachesimplexsolver;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import javax.management.relation.Relation;
import javax.management.relation.RelationService;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;

import it.naddeil.ro.common.NSimplexSolver;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Tipo;
import it.naddeil.ro.common.api.Verso;
import it.naddeil.ro.common.api.Vincolo;
import it.naddeil.ro.common.models.FracResult;

public class MySimplexSolver implements NSimplexSolver {
    private Relationship transform(Verso v){
        switch (v) {
            case LE:
                return Relationship.LEQ;
            case GE:
                return Relationship.GEQ;
            case E:
                return Relationship.EQ;    
            default:
                return null;
        }
    }

    double[] getAllButLast(List<Double> list){
        return IntStream.range(0, list.size() -1).mapToDouble(i -> list.get(i).doubleValue()).toArray();
    }
    double getLast(List<Double> list){
        return list.get(list.size() - 1).doubleValue();
    }   
    private LinearConstraint vincoloMapper(Vincolo v){
        List<Double> vincolo = v.getVincolo();
        return new LinearConstraint(getAllButLast(vincolo),  transform(v.getVerso()), getLast(vincolo));
    }
    @Override
    public FracResult solve(PublicProblem problem) {
        
        
         
        // Costruisco la funzione obbiettivo
        List<Double> c = problem.getFunzioneObbiettivo().getC();
        LinearObjectiveFunction f = new LinearObjectiveFunction(getAllButLast(c), getLast(c));
        GoalType g = Tipo.MAX.equals(problem.getFunzioneObbiettivo().getTipo()) ? GoalType.MAXIMIZE : GoalType.MINIMIZE;

        // Costruisco i vincoli
        List<LinearConstraint> vincoli = (problem.getVincoli().stream().map(this::vincoloMapper).toList());
        

        SimplexSolver solver = new SimplexSolver();
        
         try {
            return FracResult.fromTableau(MatrixBridge.transform(solver.optimizeToTab(f, vincoli, g, true).getTableau().getData()));
        } catch (OptimizationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         return null;

    }


}
