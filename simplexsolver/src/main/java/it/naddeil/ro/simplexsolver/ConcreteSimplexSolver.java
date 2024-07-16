package it.naddeil.ro.simplexsolver;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.SimplexSolver;
import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.TableauUtils;


public class ConcreteSimplexSolver implements SimplexSolver{
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
        List<Message> passaggi = new ArrayList<>();
        Fraction[][] result = DueFasi.applicaMetodoDueFasi(tableau, indiciSlack, passaggi);
        return FracResult.fromTableau(result).setOut(passaggi);
    }
}
