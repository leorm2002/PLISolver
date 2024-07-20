package it.naddeil.ro.simplexsolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import it.naddeil.ro.common.ProblemTransformer;
import it.naddeil.ro.common.SimplexSolver;
import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Tipo;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.models.Problema;
import it.naddeil.ro.common.utils.TableauUtils;
import it.naddeil.ro.common.utils.Value;

public class ConcreteSimplexSolver implements SimplexSolver {

    static void postProcess(Value[][] tableau, PublicProblem problem) {
        // Se il problema è di minimizzazione nega la prima riga
        if (Tipo.MIN.equals(problem.getFunzioneObbiettivo().getTipo())) {
            for (int i = 0; i < tableau[0].length; i++) {
                tableau[0][i] = tableau[0][i].negate();
            }
        }
    }

    public FracResult solve(PublicProblem problem) {
        // Il metodo due fasi prende in input un tableau della forma
        // min c^T x
        // Ax = b
        int numeroVariabili = problem.getVincoli().get(0).getVincolo().size() - 1;
        Value[][] tableau = Problema.fromPublic(ProblemTransformer.portaInFormaCanonica(problem)).toTableauForm(problem.getParameters());
        int numeroVariabiliSlack = tableau[0].length - 1 - numeroVariabili;
        List<Integer> indiciSlack = IntStream.range(numeroVariabili, numeroVariabili + numeroVariabiliSlack).boxed().toList();
        System.out.println("Tableau iniziale simplesso primale");
        TableauUtils.printTableau(tableau);
        System.out.println();
        List<Message> passaggi = new ArrayList<>();
        passaggi.add(Message.messaggioConTableau("Problema iniziale:", tableau));
        Value[][] result = DueFasi.applicaMetodoDueFasi(tableau, indiciSlack, passaggi);
        postProcess(tableau, problem);
        return FracResult.fromTableau(result).setOut(passaggi);
    }
}
