package it.naddeil.ro.dualsimplexsolver;
import java.util.ArrayList;
import java.util.List;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;


public class SimplessoDualeFrazionario {
    private Comp[][] tableau;
    private List<Message> passaggi = new ArrayList<>();

    public List<Message> getPassaggi() {
        return passaggi;
    }


    public Comp[][] getTableau() {
		return tableau;
	}


	private int numVariables;
    private int numConstraints;

    public SimplessoDualeFrazionario(Comp[][] initialTableau) {
        this.tableau = initialTableau;
        this.numConstraints = tableau.length - 1;
        this.numVariables = tableau[0].length - 1;
    }


    public void solve() {
        passaggi.add(Message.messaggioConTableau("Inizio risoluzione del tableau con il metodo del simplesso duale", tableau));
        while (true) {
            int r = determineRowR();
            if (r == -1) {
                System.out.println("Soluzione ottima trovata.");
                printSolution();
                passaggi.add(Message.messaggioSemplice("Soluzione ottima trovata"));
                return;
            }

            int k = determineColumnK(r);
            if (k == -1) {
                System.out.println("Il duale è illimitato, la soluzione ammissibile del primale non esiste.");
                passaggi.add(Message.messaggioSemplice("Il duale è illimitato, la soluzione ammissibile del primale non esiste."));
                return;
            }
            pivot(r, k);
            passaggi.add(Message.messaggioConTableau(String.format("Eseguo pivotaggio su X%s R%s, tableau dopo operazione:", k + 1, r), tableau));
        }
    }

    private int determineRowR() {
        int r = -1;
        Comp minValue = Comp.POSITIVE_INFINITY;
        for (int i = 1; i <= numConstraints; i++) {
            if (tableau[i][numVariables].compareTo(minValue) < 0) {
                minValue = tableau[i][numVariables];
                r = i;
            }
        }
        return (minValue.compareTo(Comp.ZERO) < 0) ? r : -1;
    }

    private int determineColumnK(int r) {
        int k = -1;
        Comp minRatio = Comp.POSITIVE_INFINITY;
        for (int j = 0; j < numVariables; j++) {
            if (tableau[r][j].compareTo(Comp.ZERO) < 0) {
                Comp ratio = tableau[0][j].divide(tableau[r][j]).abs();
                if (ratio.compareTo(minRatio) < 0 || (ratio.equals(minRatio) && j < k)) {  // Regola di Bland
                    minRatio = ratio;
                    k = j;
                }
            }
        }
        return k;
    }

    private void pivot(int r, int k) {
        Comp pivotElement = tableau[r][k];
        for (int j = 0; j <= numVariables; j++) {
            tableau[r][j] = tableau[r][j].divide(pivotElement);
        }

        for (int i = 0; i <= numConstraints; i++) {
            if (i != r) {
                Comp factor = tableau[i][k];
                for (int j = 0; j <= numVariables; j++) {
                    tableau[i][j] = tableau[i][j].subtract(factor.multiply(tableau[r][j]));
                }
            }
        }
    }

    private void printSolution() {
        System.out.println("Soluzione ottima:");
        for (int j = 0; j < numVariables - 1; j++) { // Escludi la variabile artificiale
            boolean isBasic = false;
            Comp value = Comp.ZERO;
            for (int i = 1; i <= numConstraints; i++) {
                if (isUnitVector(j) && tableau[i][j].equals(Comp.ONE)) {
                    isBasic = true;
                    value = tableau[i][numVariables];
                    break;
                }
            }
            if (isBasic) {
                System.out.printf("x%d = %s%n", j + 1, value);
            } else {
                System.out.printf("x%d = 0%n", j + 1);
            }
        }
        System.out.printf("Valore ottimo della funzione obiettivo: %s%n", tableau[0][numVariables].negate());
    }

    private boolean isUnitVector(int col) {
        int oneCount = 0;
        for (int i = 1; i <= numConstraints; i++) {
            if (tableau[i][col].equals(Comp.ONE)) {
                oneCount++;
            } else if (!tableau[i][col].equals(Comp.ZERO)) {
                return false;
            }
        }
        return oneCount == 1;
    }

    public void printTableau() {
        for (Comp[] row : tableau) {
            for (Comp val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    
        public static void main(String[] args) {
            Fraction[][] tableau = {
                {new Fraction(-2), new Fraction(-3), new Fraction(-4), Comp.ZERO, Comp.ZERO, Comp.ZERO},
                {new Fraction(-1), new Fraction(-2), new Fraction(-1), Comp.ONE, Comp.ZERO, new Fraction(-3)},
                {new Fraction(-2), Comp.ONE, new Fraction(-3), Comp.ZERO, Comp.ONE, new Fraction(-4)}
            };

          
            SimplessoDualeFrazionario simplesso = new SimplessoDualeFrazionario(tableau);
            System.out.println("Tableau iniziale:");
            simplesso.printTableau();
            simplesso.solve();
        }
    
    }