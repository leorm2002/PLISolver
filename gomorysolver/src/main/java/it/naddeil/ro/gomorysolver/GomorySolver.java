package it.naddeil.ro.gomorysolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.naddeil.ro.common.SimplexSolver;
import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.exceptions.ExceptionUtils;
import it.naddeil.ro.common.exceptions.NumeroMassimoIterazioni;
import it.naddeil.ro.common.models.Pair;
import it.naddeil.ro.common.models.Result;
import it.naddeil.ro.common.utils.Value;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;

public class GomorySolver {
    private final SimplexSolver simplexSolver;
    private final DualSimplexSolver dualSimplexSolver;
    private final List<Message> out;

    // Vincoli:
    // Tutti i segni della funzione obbiettivo positivi
    public GomorySolver(SimplexSolver simplexSolver, DualSimplexSolver dualSimplexSolver) {
        this.simplexSolver = simplexSolver;
        this.dualSimplexSolver = dualSimplexSolver;
        this.out = new LinkedList<>();
    }

    boolean isSolved(Result r) {
        Value[] s = r.getSoluzione();
        for (int i = 0; i < s.length; i++) {
            if (!s[i].isInteger()) {
                return false;
            }
        }
        return true;
    }

    static Value f(Value x) {
        return x.subtract(x.floor());
    }

    public static Value[] creaTaglio(Value[] riga) {
        // Un taglio è del tipo f(c[1])x_1 + f(c[2])x_2 + ... + f(c[n])x_n >= f(c[0])
        // Dove f(x) è definita come x -> x - floor(x)
        Value[] taglio = new Value[riga.length];
        for (int i = 0; i < riga.length; i++) {
            taglio[i] = f(riga[i]);
        }
        return taglio;
    }

    public static int trovaRigaConValoreFrazionario(Value[] matrice) {
        int riga = -1;
        for (int i = 0; i < matrice.length; i++) {
            if (!matrice[i].isInteger()) {
                riga = i + 1;
                break;
            }
        }
        return riga;
    }

    Value[][] aggiungiVincolo(Value[][] tableau, Value[] taglio, Value[] b, Value z) {
        int nRighe = tableau.length;
        int nColonne = tableau[0].length;
        Value[][] nuovaMatrice = new Value[nRighe + 1][nColonne + 1];

        // Ricopio la matrice settando 0 alla fine di ogni riga
        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne - 1; j++) {
                nuovaMatrice[i][j] = tableau[i][j];
            }
            nuovaMatrice[i][nColonne - 1] = Value.ZERO; // Aggiungi 0 alla fine
        }

        // Aggiungo il taglio alla fine
        for (int i = 0; i < nColonne - 1; i++) {
            nuovaMatrice[nRighe][i] = taglio[i];
        }
        // Variabile di slack per il taglio
        nuovaMatrice[nRighe][nColonne - 1] = Value.ONE;
        // Riporto il valore di b per il taglio
        nuovaMatrice[nRighe][nColonne] = taglio[taglio.length - 1];
        // Riporto soluzione
        nuovaMatrice[0][nColonne] = z;

        // Riporto il vettore b
        for (int i = 1; i < nRighe; i++) {
            nuovaMatrice[i][nColonne] = b[i - 1];
        }
        return nuovaMatrice;
    }

    Pair<Value[], Integer> creaTaglio(Value[] b, Value[][] tableau) {
        // 0. Estraggo l'ultima colonna ovvero i valori di b
        // 1. trova riga su cui aggiungere taglio (riga con coefficiente frazionario più grande)
        int rigaTaglio = trovaRigaConValoreFrazionario(b);
        Value[] riga = tableau[rigaTaglio];

        // 2. calcola taglio
        return Pair.of(Arrays.stream(creaTaglio(riga)).map(Value::negate).toArray(Value[]::new), rigaTaglio);

    }

    public void printTableau(Value[][] tableau) {
        for (Value[] row : tableau) {
            for (Value val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printTableauD(Value[][] tableau) {
        for (Value[] row : tableau) {
            for (Value val : row) {
                System.out.print(val.doubleValue() + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Result solve(PublicProblem problem) {
        final int maxIter = problem.getParameters().getMaxIterazioni();
        final boolean passaggiIntermedi = Boolean.TRUE.equals(problem.getParameters().getPassaggiIntermedi());
        out.add(Message.messaggioSemplice("Inizio risoluzione problema, numero massimo iterazioni:" + maxIter));
        out.add(Message.messaggioSemplice("Risolvo il rilassamento continuo con il metodo del simplesso"));

        Result rs = ExceptionUtils.catchAndRetrow(() -> simplexSolver.solve(problem), out);
        if (passaggiIntermedi) {
            out.add(Message.conPassaggiIntermedi(rs.getOut()));
            out.add(Message.messaggioConTableau("Soluzione del rilassamento continuo", rs.getTableau()));
        }

        int i = 0;
        while (true) {
            if (!isSolved(rs)) {
                System.out.println("Soluzione non intera, aggiungo taglio");
                out.add(Message.messaggioSemplice("Soluzione non intera, aggiungo taglio"));
                // Calcolo il taglio
                var cutResult = creaTaglio(rs.getSoluzione(), rs.getTableau());
                Value[] taglio = cutResult.getFirst();
                out.add(Message.messaggioConTaglio(cutResult.getSecond(), taglio));
                Value[][] newA = aggiungiVincolo(rs.getTableau(), taglio, rs.getSoluzione(), rs.getZ());
                out.add(Message.messaggioConTableau("Tableau ottimo rilassamento continuo dopo aggiunta taglio", newA));
                rs = ExceptionUtils.catchAndRetrow(() -> dualSimplexSolver.riottimizza(newA), out);
                if (passaggiIntermedi) {
                    out.add(Message.conPassaggiIntermedi(rs.getOut()));
                }
                out.add(Message.messaggioConRisultato("Risulato dell'ottimizzazione del nuovo tableau", rs));
                if (isSolved(rs)) {
                    out.add(Message.messaggioSemplice("Soluzione intera trovata"));
                    return rs.setOut(out);
                }
                i++;
                if (i > maxIter) {
                    out.add(Message.messaggioSemplice("Numero massimo iterazioni raggiunto"));
                    throw new NumeroMassimoIterazioni(out);
                }
            } else {
                out.add(Message.messaggioSemplice("Soluzione intera trovata"));
                return rs.setOut(out);
            }
        }
    }
}
