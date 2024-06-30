package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.api.Pair;

public class Solver {

    static int findInvalidColumn(Tableau t) {
        // 2. Le variabili in base devono avere costi ridotti nulli
        double[] primaColonna = t.getMatrix()[0];
        var base = t.getBase();
        for (int j = 0; j < base.length; j++) {
            if (primaColonna[base[j]] != 0) {
                return base[j];
            }
        }
        return -1;
    }

    public void solve(Tableau t) {
        // 1. Inizializza tableau
        // 2. Se esiste variabile in base con valore non intero o con valori sopra o sotto diversi da 0 porta a 1 e azzera colonna
        int colonnaInvalida = findInvalidColumn(t);
        if (colonnaInvalida != -1) {
            t.azzera(colonnaInvalida);
        }

        while (!t.isOttimo()) {
            Pair newBase = Bland.getNewBase(t);
            t.portaAUno(newBase.getI());
            t.azzera(newBase.getI());
        }
    }
}
