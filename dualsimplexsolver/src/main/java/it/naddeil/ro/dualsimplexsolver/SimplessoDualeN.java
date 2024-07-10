package it.naddeil.ro.dualsimplexsolver;

public class SimplessoDualeN {
    private double[][] tableau;
    private int numVariables;
    private int numConstraints;

    public SimplessoDualeN(double[][] initialTableau) {
        this.tableau = initialTableau;
        this.numConstraints = initialTableau.length - 1;
        this.numVariables = initialTableau[0].length - 1;
    }

    public void solve() {
        while (true) {
            // Step 2: Determinazione riga r
            int r = determineRowR();
            if (r == -1) {
                System.out.println("Soluzione ottima trovata.");
                printSolution();
                return;
            }

            // Step 3: Determinazione colonna k
            int k = determineColumnK(r);
            if (k == -1) {
                System.out.println("Il duale è illimitato, la soluzione ammissibile del primale non esiste.");
                return;
            }

            // Step 4: Pivoting
            pivot(r, k);
        }
    }

    private int determineRowR() {
        int r = -1;
        double minValue = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= numConstraints; i++) {
            if (tableau[i][numVariables] < minValue) {
                minValue = tableau[i][numVariables];
                r = i;
            }
        }
        return (minValue < 0) ? r : -1;
    }

    private int determineColumnK(int r) {
        int k = -1;
        double minRatio = Double.POSITIVE_INFINITY;
        for (int j = 0; j < numVariables; j++) {
            if (tableau[r][j] < 0) {
                double ratio = Math.abs(tableau[0][j] / tableau[r][j]);
                if (ratio < minRatio) {
                    minRatio = ratio;
                    k = j;
                }
            }
        }
        return k;
    }

    private void pivot(int r, int k) {
        // Elemento di pivot
        double pivotElement = tableau[r][k];

        // Aggiorna la riga di pivot
        for (int j = 0; j <= numVariables; j++) {
            tableau[r][j] /= pivotElement;
        }

        // Aggiorna le altre righe
        for (int i = 0; i <= numConstraints; i++) {
            if (i != r) {
                double factor = tableau[i][k];
                for (int j = 0; j <= numVariables; j++) {
                    tableau[i][j] -= factor * tableau[r][j];
                }
            }
        }
    }

    private void printSolution() {
        System.out.println("Soluzione ottima:");
        for (int j = 0; j < numVariables; j++) {
            boolean isBasic = false;
            double value = 0;
            for (int i = 1; i <= numConstraints; i++) {
                if (isUnitVector(i, j)) {
                    isBasic = true;
                    value = tableau[i][numVariables];
                    break;
                }
            }
            if (isBasic) {
                System.out.printf("x%d = %.4f%n", j + 1, value);
            } else {
                System.out.printf("x%d = 0%n", j + 1);
            }
        }
        System.out.printf("Valore ottimo della funzione obiettivo: %.4f%n", -tableau[0][numVariables]);
        // Stampa  il tableau finale
        System.out.println("Tableau finale:");
        new Printer().printTableauFractions(tableau);
        
    }

    private boolean isUnitVector(int row, int col) {
        for (int i = 1; i <= numConstraints; i++) {
            if (i == row) {
                if (Math.abs(tableau[i][col] - 1) > 1e-6) return false;
            } else {
                if (Math.abs(tableau[i][col]) > 1e-6) return false;
            }
        }
        return true;
    }

    public void printTableau() {
        for (double[] row : tableau) {
            for (double val : row) {
                System.out.printf("%8.4f ", val);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Esempio di utilizzo
        double[][] initialTableau = {
            {-2, -3, -4, 0, 0, 0},
            {-1, -2, -1, 1, 0, -3},
            {-2, 1, -3, 0, 1, -4}
        };
        SimplessoDualeN simplesso = new SimplessoDualeN(initialTableau);
        System.out.println("Tableau iniziale:");
        simplesso.printTableau();
        simplesso.solve();

    }
}