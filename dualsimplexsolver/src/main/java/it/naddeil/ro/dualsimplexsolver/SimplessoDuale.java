package it.naddeil.ro.dualsimplexsolver;

import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.List;


import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.Result;


public class SimplessoDuale {

    SimpleMatrix A; // Matrice dei coefficienti
    SimpleMatrix updatedA; // Matrice dei coefficienti
    SimpleMatrix b; // Vettore dei termini noti
    SimpleMatrix c; // Vettore dei coefficienti della funzione obiettivo
    List<Integer> basis; // Indici delle variabili di base
    

	SimpleMatrix solution;
    SimpleMatrix cout;
    SimpleMatrix bout;

    private SimplessoDuale(SimpleMatrix A, SimpleMatrix b, SimpleMatrix c) {
        this.A = A;
        this.b = b;
        this.c = c;
        this.basis = new ArrayList<>();
        initializeBasis();
    }

    public Result buildResult(SimpleMatrix solution) {
        SimpleMatrix out = new SimpleMatrix(A.numRows()+ 1, A.numCols() + 1);
        // Se first row to c
        for (int i = 0; i < c.numCols(); i++) {
            out.set(0, i, cout.get(i));
        }
        // Set last column to b
        for (int i = 0; i < b.numRows(); i++) {
            out.set(i + 1, A.numCols(), bout.get(i));
        }
        // Set the rest to A
        for (int i = 0; i < A.numRows(); i++) {
            for (int j = 0; j < A.numCols(); j++) {
                out.set(i + 1, j, updatedA.get(i, j));
            }
        }
        
        return new DualSimplexResult( out, solution, basis);
    }

    public static SimplessoDuale createFromTableau(SimpleMatrix A, SimpleMatrix b, SimpleMatrix c, List<Integer> basis) {
        SimplessoDuale s = new SimplessoDuale(A, b, c);
        if(basis != null && !basis.isEmpty()){
            s.basis = basis;
        }
        return s;
    }

    /**
     * Prende un problema del tipo:
     * min c^T x
     * s.t. Ax >= b
     * @param A
     * @param b
     * @param c
     * @return
     */
    public static SimplessoDuale createFromCanonical(SimpleMatrix A, SimpleMatrix b, SimpleMatrix c) {
        c = c.negative();
        c = aggiungiSlack(c, A.numRows());
       
        A = A.negative();
        A = aggiungiSlack(A);
        
        b = b.negative();

        return new SimplessoDuale(A, b, c);
    }



    private void initializeBasis() {
        // Inizializza la base con le ultime m variabili (assumendo che siano le variabili di slack)
        for (int i = A.numCols() - A.numRows(); i < A.numCols(); i++) {
            basis.add(i);
        }
    }

    public SimpleMatrix solve() {
        updatedA = A.copy();
        while (true) {
            SimpleMatrix B = estraiBase(); // Matrice delle variabili di base
            SimpleMatrix b_bar = B.solve(b); // calcolo b = B^-1 * b
            bout = b_bar;

            // Calcola i costi ridotti
            SimpleMatrix y = B.transpose().solve(extractCB());
            SimpleMatrix Ay = A.transpose().mult(y);
            SimpleMatrix cBar = c.minus(Ay.transpose()); // Costi ridotti
            cout = cBar;

            if (isNonNegative(b_bar)) {
                // Se b_bar è non negativo, allora la soluzione è ottima
                return reconstructSolution(b_bar);
            }

            // Trova riga r su cui fare pivot r = min(r in b bar)
            int r = findMostNegativeIndex(b_bar); 
            // Estraggo d = updatedA[r]
            SimpleMatrix d = updatedA.extractVector(true, r);

            // Ricerca la variabile entrante che minimizzi il rapport otra costo ridotto e il valore negativo della riga pivot
            // Se non esiste il duale è illimitato e il primale è inammissibile
            int s = findEnteringVariable(cBar, d);
            updateBasis(r, s);
            updatedA = updateMatrixA(B, updatedA, r, s);
        }
    }

    private SimpleMatrix updateMatrixA(SimpleMatrix B, SimpleMatrix A, int r, int s) {
        SimpleMatrix entering = A.extractVector(true, r);
        double scale = entering.get(s);

        // A[r] = A[r] / scale
        for (int i = 0; i < A.numCols(); i++) {
            A.set(r, i, A.get(r, i) / scale);
        }
        // Per ogni riga i != r fai A[i] = A[i] - A[r] * A[i][s]
        for (int i = 0; i < A.numRows(); i++) {
            if (i != r) {
                SimpleMatrix row = A.extractVector(true, i);
                double factor = A.get(i, s);
                for (int j = 0; j < A.numCols(); j++) {
                    A.set(i, j, row.get(j) - factor * A.get(r, j));
                }
            }
        }
        
        return A;
    }

    private boolean isNonNegative(SimpleMatrix x) {
        // Se è un vettore
        if (x.numCols() == 1) {
            for (int i = 0; i < x.numRows(); i++) {
                if (x.get(i) < 0) {
                    return false;
                }
            }
            return true;
        }else{

            // Se è una matrice
            for (int i = 0; i < x.numRows(); i++) {
                for (int j = 0; j < x.numCols(); j++) {
                    if (x.get(i, j) < 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Estrae una sottromanice di A composta dalle colonne indicate dagli indici della base.
     * @return
     */
    private SimpleMatrix estraiBase() {
        if (basis.size() != A.numRows()) {
            throw new IllegalStateException("La dimensione della base non corrisponde al numero di righe di A.");
        }
        
        SimpleMatrix B = new SimpleMatrix(A.numRows(), A.numRows());
        for (int i = 0; i < basis.size(); i++) {
            SimpleMatrix column = A.extractVector(false, basis.get(i));
            for (int j = 0; j < A.numRows(); j++) {
                B.set(j, i, column.get(j,0));
            }
        }
        return B;
    }

    private SimpleMatrix extractCB() {
        SimpleMatrix cB = new SimpleMatrix(A.numRows(), 1);
        for (int i = 0; i < basis.size(); i++) {
            cB.set(i, c.get(basis.get(i)));
        }
        return cB;
    }

    private int findMostNegativeIndex(SimpleMatrix x) {
        int index = 0;
        double minValue = Double.MAX_VALUE;
        for (int i = 0; i < x.numRows(); i++) {
            if (x.get(i) < minValue) {
                minValue = x.get(i);
                index = i;
            }
        }
        return index;
    }
    
    private int findEnteringVariable(SimpleMatrix cBar, SimpleMatrix y) {
        // Valori non zero della riga / valore negativo delle riga pivot
        int index = -1;
        double minRatio = Double.MAX_VALUE;
        for (int j = 0; j < cBar.numCols(); j++) {
            if (y.get(j) < 0){
                double ratio = cBar.get(j) / (y.get(j));
                if (ratio < minRatio) {
                    minRatio = ratio;
                    index = j;
                }
            }
        }
        if (index == -1) {
            throw new IllegalStateException("Non è possibile trovare una variabile entrante.");
        }
        return index;
    }

    private void updateBasis(int r, int s) {
        basis.set(r, s);
    }

    private SimpleMatrix reconstructSolution(SimpleMatrix xB) {
        SimpleMatrix x = new SimpleMatrix(A.numCols(), 1);
        for (int i = 0; i < basis.size(); i++) {
            x.set(basis.get(i), xB.get(i));
        }
        this.solution = x;
        return x;
    }


    private static SimpleMatrix aggiungiSlack(SimpleMatrix A){
        int n = A.numRows();
        SimpleMatrix I = SimpleMatrix.identity(n);
        return A.combine(0, A.numCols(), I);
    }

    private static SimpleMatrix aggiungiSlack(SimpleMatrix d, int n){
        // Creo un vettore di lunghezza n con tutti zeri
        SimpleMatrix zero = new SimpleMatrix( 1, n);
        // Lo combino con il vettore d
        return d.combine(0, d.numCols(), zero);
    }


    public static void main(String[] args) {
        // Esempio di utilizzo
        // Problema iniziale:

        // min 3x +4y
        // s.t.
        // 2x + y <= 600
        // x + y <= 225
        // 5x + 4y <= 1000
        // x + x2 >= 150


        SimpleMatrix A = new SimpleMatrix(new double[][] {
            {2, 1},
            {1, 1},
            {5, 4},
            {-1, -2},
        });
        SimpleMatrix b = new SimpleMatrix(new double[][] {{600}, {225}, {1000}, {-150}});
        SimpleMatrix c = new SimpleMatrix(new double[][] {{-3,-4}});

        c = aggiungiSlack(c, A.numRows());
        A = aggiungiSlack(A);

        A = new SimpleMatrix(new double[][] {
            {1, 2, 1},
            {2, -1, 3},
        });

        c = new SimpleMatrix(new double[][] {{2, 3, 4}});
        b = new SimpleMatrix(new double[][] {{3}, {4}});


        SimplessoDuale dualSimplex = SimplessoDuale.createFromCanonical(A, b, c);
        SimpleMatrix solution = dualSimplex.solve();

        System.out.println("Soluzione ottima:");
        System.out.println(solution);

        Result result = dualSimplex.buildResult(solution);
        System.out.println("Tableau ottimo:");
        System.out.println(result.getTableauOttimo());
    }
}