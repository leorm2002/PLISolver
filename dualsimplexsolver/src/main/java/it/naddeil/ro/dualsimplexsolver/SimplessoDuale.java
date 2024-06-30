package it.naddeil.ro.dualsimplexsolver;

import java.util.Arrays;

import it.naddeil.ro.api.MatrixUtils;

public class SimplessoDuale {
    private double[][] A; // Matrice dei coefficienti
    private double[] b; // Vettore dei termini noti
    private double[] c; // Vettore dei coefficienti della funzione obiettivo
    private int m, n; // m = numero di vincoli, n = numero di variabili

    public SimplessoDuale(double[][] A, double[] b, double[] c) {
        this.A = A;
        this.b = b;
        this.c = c;
        this.m = A.length;
        this.n = A[0].length;
    }

    public double[] risolvi() {
        double[] x = new double[n];
        int[] base = new int[m];
        for (int i = 0; i < m; i++) {
            base[i] = n + i; // Inizializza la base con le variabili di slack
        }

        while (true) {
            // Trova la riga con la violazione primale più grande
            int rigaUscita = -1;
            double maxViolazione = 0;
            for (int i = 0; i < m; i++) {
                if (b[i] < maxViolazione) {
                    maxViolazione = b[i];
                    rigaUscita = i;
                }
            }

            if (rigaUscita == -1) {
                // Soluzione ottimale trovata
                return x;
            }

            // Trova la colonna di entrata
            int colonnaEntrata = -1;
            double minRapporto = Double.POSITIVE_INFINITY;
            for (int j = 0; j < n; j++) {
                if (A[rigaUscita][j] > 0) {
                    double rapporto = c[j] / A[rigaUscita][j];
                    if (rapporto < minRapporto) {
                        minRapporto = rapporto;
                        colonnaEntrata = j;
                    }
                }
            }

            if (colonnaEntrata == -1) {
                // Problema inammissibile
                throw new RuntimeException("Problema inammissibile");
            }

            // Esegui l'operazione di pivot
            double pivotElement = A[rigaUscita][colonnaEntrata];
            for (int j = 0; j < n; j++) {
                A[rigaUscita][j] /= pivotElement;
            }
            b[rigaUscita] /= pivotElement;

            for (int i = 0; i < m; i++) {
                if (i != rigaUscita) {
                    double factor = A[i][colonnaEntrata];
                    for (int j = 0; j < n; j++) {
                        A[i][j] -= factor * A[rigaUscita][j];
                    }
                    b[i] -= factor * b[rigaUscita];
                }
            }

            // Aggiorna la base
            base[rigaUscita] = colonnaEntrata;

            // Aggiorna la soluzione
            Arrays.fill(x, 0);
            for (int i = 0; i < m; i++) {
                if (base[i] < n) {
                    x[base[i]] = b[i];
                }
            }
        }
    }

    public static void main(String[] args) {
        // Problema primale in forma
        // min cTx
        // Ax >= b
        double[] c = {-3, -2 };
        double[][] A = { { 1, 2 }, { 4, 3 } };
        double[] b = { 12, 4 };

        // Costruiamo il duale
        double[][] A_t = MatrixUtils.transpose(A);
        double[] c_t = b;
        double[] b_t = c;

        // Aggiungiamo slack
        double[] r0 = { 1, 4, 1, 0 };
        double[] r1 = { 2, 3, 0, 1 };
        //A_t = new double[][] { r0, r1 };

        //c_t = new double[] { c_t[0], c_t[1], 0, 0 };

        SimplessoDuale sd = new SimplessoDuale(A, b, c);
        double[] soluzione = sd.risolvi();

        System.out.println("Soluzione ottimale:");
        for (int i = 0; i < soluzione.length; i++) {
            System.out.printf("x%d = %.2f%n", i + 1, soluzione[i]);
        }
    }
}