package it.naddeil.ro.simplexsolver;

import java.util.*;

public class SimplexTwoPhase {
    private double[][] tableau;
    private int rows, cols;
    private Set<Integer> basis;

    public SimplexTwoPhase(double[][] tableau) {
        this.tableau = tableau;
        this.rows = tableau.length;
        this.cols = tableau[0].length;
        this.basis = new HashSet<>();
    }

    public double[] solve() {
        if (!hasInitialBasis()) {
            performPhaseOne();
        }
        return performPhaseTwo();
    }

    private boolean hasInitialBasis() {
        basis.clear();
        for (int j = 0; j < cols - 1; j++) {
            int unitColumn = isUnitColumn(j);
            if (unitColumn != -1) {
                basis.add(unitColumn);
            }
        }
        return basis.size() == rows - 1;
    }

    private int isUnitColumn(int col) {
        int oneCount = 0;
        int oneRow = -1;
        for (int i = 0; i < rows; i++) {
            if (tableau[i][col] == 1) {
                oneCount++;
                oneRow = i;
            } else if (tableau[i][col] != 0) {
                return -1;
            }
        }
        return oneCount == 1 ? oneRow : -1;
    }

    private void performPhaseOne() {
        int artificialVars = rows - 1 - basis.size();
        double[][] extendedTableau = new double[rows][cols + artificialVars];
        
        for (int i = 0; i < rows; i++) {
            System.arraycopy(tableau[i], 0, extendedTableau[i], 0, cols);
        }

        int artificialIndex = cols - 1;
        for (int i = 1; i < rows; i++) {
            if (!basis.contains(i)) {
                extendedTableau[i][artificialIndex] = 1;
                basis.add(artificialIndex);
                artificialIndex++;
            }
        }

        double[] artificialObjective = new double[cols + artificialVars];
        for (int j = cols - 1; j < cols + artificialVars - 1; j++) {
            artificialObjective[j] = -1;
        }

        for (int j = 0; j < cols + artificialVars; j++) {
            double sum = 0;
            for (int i = 1; i < rows; i++) {
                sum += extendedTableau[i][j];
            }
            artificialObjective[j] += sum;
        }

        extendedTableau[0] = artificialObjective;

        tableau = extendedTableau;
        cols = tableau[0].length;

        optimize();

        if (Math.abs(tableau[0][cols - 1]) > 1e-10) {
            throw new IllegalStateException("Il problema non ha soluzioni ammissibili.");
        }

        removeArtificialVariables();
    }

    private void removeArtificialVariables() {
        int originalCols = cols - (rows - 1 - basis.size());
        double[][] newTableau = new double[rows][originalCols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(tableau[i], 0, newTableau[i], 0, originalCols);
        }

        tableau = newTableau;
        cols = originalCols;
        
        basis.removeIf(b -> b >= originalCols - 1);
    }

    private double[] performPhaseTwo() {
        //System.arraycopy(tableau[rows - 1], 0, tableau[0], 0, cols);
        //tableau[0][cols - 1] = 0;

        optimize();

        double[] solution = new double[cols - 1];
        for (int j = 0; j < cols - 1; j++) {
            int basicRow = isUnitColumn(j);
            if (basicRow != -1) {
                solution[j] = tableau[basicRow][cols - 1];
            }
        }
        return solution;
    }

    private void optimize() {
        while (true) {
            int pivotCol = selectPivotColumn();
            if (pivotCol == -1) break;

            int pivotRow = selectPivotRow(pivotCol);
            if (pivotRow == -1) {
                throw new IllegalStateException("Il problema è illimitato.");
            }

            pivot(pivotRow, pivotCol);
        }
    }

    private int selectPivotColumn() {
        for (int j = 0; j < cols - 1; j++) {
            if (tableau[0][j] < 0) {
                return j;
            }
        }
        return -1;
    }

    private int selectPivotRow(int pivotCol) {
        int pivotRow = -1;
        double minRatio = Double.POSITIVE_INFINITY;
        for (int i = 1; i < rows; i++) {
            if (tableau[i][pivotCol] > 0) {
                double ratio = tableau[i][cols - 1] / tableau[i][pivotCol];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    private void pivot(int pivotRow, int pivotCol) {
        double pivotElement = tableau[pivotRow][pivotCol];

        for (int j = 0; j < cols; j++) {
            tableau[pivotRow][j] /= pivotElement;
        }

        for (int i = 0; i < rows; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotCol];
                for (int j = 0; j < cols; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }

        basis.remove(pivotRow);
        basis.add(pivotCol);
    }

    public static void main(String[] args) {
        double[][] tableau = {
            {2, 3, 4, 0, 0, 0},
            {-1, -2, -1, 1, 0, -3},
            {-2, 1, -3, 0, 1,-4}
        };
    
        SimplexTwoPhase simplex = new SimplexTwoPhase(tableau);
        double[] solution = simplex.solve();
    
        System.out.println("Soluzione ottima:");
        for (int i = 0; i < solution.length; i++) {
            System.out.printf("x%d = %.2f%n", i + 1, solution[i]);
        }
    }
}