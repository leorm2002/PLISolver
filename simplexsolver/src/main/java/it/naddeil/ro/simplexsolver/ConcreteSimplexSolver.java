package it.naddeil.ro.simplexsolver;

import java.util.List;
import java.util.stream.IntStream;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.NSimplexSolver;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.TableauUtils;


public class ConcreteSimplexSolver implements NSimplexSolver{
    public FracResult solve(PublicProblem problem) {
        // Il metodo due fasi prende in input un tableau della forma
        // min c^T x
        // Ax = b
        int numeroVariabili = problem.getVincoli().get(0).getVincolo().size() - 1;
        Fraction[][] tableau =  Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problem)).toTableauFormProblem();
        int numeroVariabiliSlack = tableau[0].length - 1 - numeroVariabili;
        List<Integer> indiciSlack = IntStream.range(numeroVariabili, numeroVariabili + numeroVariabiliSlack).boxed().toList();
                System.out.println("Tableau iniziale simplesso primale");
        TableauUtils.printTableau(tableau);
        System.out.println();
        Fraction[][] result = DueFasi.applicaMetodoDueFasi(tableau, indiciSlack);
        return FracResult.fromTableau(result);
    }
}
