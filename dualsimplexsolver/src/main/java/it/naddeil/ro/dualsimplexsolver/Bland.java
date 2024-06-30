package it.naddeil.ro.dualsimplexsolver;

import java.util.ArrayList;
import java.util.List;

import it.naddeil.ro.api.Pair;

public class Bland {

    /*
     * Ritorna la colonna su cui effettuare il pivot, secondo la regola di Bland ovvero la prima colonna con un coefficiente negativo
     */
    static int findColumn(double[] c) {
        for (int i = 0; i < c.length; i++) {
            if (c[i] < 0) {
                return i + 1;
            }
        }
        return -1;
    }

    static int findRow(int column, Tableau t) {
        double min = Double.MAX_VALUE;
        double[][] matrix = t.getMatrix();
        double[] b = t.getB();
        List<Integer> rows = new ArrayList<>();
        int row = -1;
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i][column] > 0) {
                double ratio = b[i - 1] / matrix[i][column];
                if (ratio < min) {
                    min = ratio;
                    row = i;
                    rows.clear();
                    rows.add(i);
                } else if (ratio == min) {
                    rows.add(i);
                }
            }
        }
        // Se esiste solo una riga con il minimo rapporto, ritorna quella
        if (rows.size() == 1) {
            return row;
        }
        // Se non esiste allora vuol dire che il problema è illimitato
        if (rows.isEmpty()) {
            throw new IllegalStateException("Problema illimitato");
        }
        // Se esistono più righe con lo stesso rapporto, ritorniamo la riga che fa uscire la variabile di indice minore
        // TODO: implementare regola dell variabile di indice minore
        min = Integer.MAX_VALUE;
        row = -1;
        for (int i : rows) {
            // Ottieni indice della variabile in base sulla colonna i
            int variabileUscita = t.getBaseIndexFromRow(i);
            if (variabileUscita < min) {
                min = variabileUscita;
                row = i;
            }
        }
        return row;

    }

    public static Pair getNewBase(Tableau tableau) {

        int column =  findColumn(tableau.getObjectiveFunction());
        if (column == -1) {
            // TODO gestire caso in cui non esistono colonne negative
            return null;
        }
        int row = findRow(column, tableau);

        int variabileUscita = tableau.getBaseIndexFromRow(row);
        int variabileEntrante = column;

        return new Pair(variabileEntrante, variabileUscita);
    }
}
