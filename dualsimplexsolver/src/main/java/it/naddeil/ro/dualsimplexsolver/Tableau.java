package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.api.MatrixUtils;

public class Tableau {
    private double[][] matrix; // Matrice del tableau

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    private int[] base; // Vettore delle variabili di base
    private int[] nonBase; // Vettore delle variabili non di base

    public Tableau(double[][] matrix) {
        this.matrix = matrix;
        int numberOfBases = matrix.length - 1;
        int numberOfVariables = matrix[0].length - 1;

        base = new int[numberOfBases];
        nonBase = new int[numberOfVariables - numberOfBases];
        try {
            buildBase();
        } catch (Exception e) {
            throw new IllegalArgumentException("Impossibile trovare una base nella matrice");
        }
    }

    public Tableau(double[][] matrix, int[] base, int[] nonBase) {
        this.matrix = matrix;
        this.base = base;
        this.nonBase = nonBase;
    }

    private void buildBase() {
        int j = 0;
        int k = 0;
        for (int i = 1; i < matrix[0].length; i++) {
            if (isBase(i)) {
                this.base[j++] = i;
            } else {
                this.nonBase[k++] = i;
            }

        }
    }

    public void azzera(int colonna) {
        int rigaBase = getBaseRow(colonna);
        azzera(colonna, rigaBase);

    }

    public void azzera(int colonna, int rigaBase) {
        // Per ogni riga i diversa da rigaBase, se il valore è diverso da zero allora sottrai a riga i rigaBase * i
        for (int i = 0; i < matrix.length; i++) {
            if (i != rigaBase && matrix[i][colonna] != 0) {
                double scalar = matrix[i][colonna];
                this.matrix = MatrixUtils.addRow(matrix, i, rigaBase, -scalar);
            }
        }

    }

    public void portaAUno(int colonna) {
        int rigaBase = getBaseRow(colonna);
        portaAUno(colonna, rigaBase);
    }

    public void portaAUno(int colonna, int riga) {
        this.matrix = MatrixUtils.multiplyRow(matrix, riga, 1 / matrix[riga][colonna]);
    }

    public boolean isOttimo() {
        double[] costiRidotti = getObjectiveFunction();
        for (int i = 0; i < costiRidotti.length; i++) {
            if (costiRidotti[i] < 0) {
                return false;
            }
        }
        return true;
    }

    /*
     * Data una riga i restituisce l'indice della variabile di base corrispondente
     */
    public int getBaseIndexFromRow(int row) {
        for (int i = 0; i < base.length; i++) {
            int colonna = base[i];
            // Veridico se la riga i ha un 1 nella colonna corrispondente alla variabile di base
            if (matrix[row][colonna] == 1) {
                return colonna;
            }
        }
        throw new IllegalStateException("Riga " + row + " non ha una variabile di base");
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getObjectiveFunction() {
        double[] firstRow = matrix[0];
        double[] ret = new double[firstRow.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = firstRow[i + 1];
        }
        return ret;
    }

    // Vettore dei termini noti
    public double[] getB() {
        double[] b = new double[matrix.length - 1];
        for (int i = 0; i < b.length; i++) {
            b[i] = matrix[i + 1][0];
        }
        return b;
    }

    public int[] getBase() {
        return base;
    }

    public int[] getNonBase() {
        return nonBase;
    }

    public double getZ() {
        return matrix[0][0];
    }

    public boolean isBase(int i) {
        int numberOfOne = 0;
        for (int j = 1; j < matrix.length; j++) {
            double val = matrix[j][i];
            if (val == 1.0) {
                numberOfOne++;
            } else if (val != 0.0) {
                return false;
            }
        }
        return numberOfOne == 1;
    }

    public int getBaseRow(int i) {
        for (int j = 1; j < matrix.length; j++) {
            double val = matrix[j][i];
            if (val == 1.0) {
                return j;
            }
        }
        return -1;
    }

    public Tableau copy() {
        double[][] newMatrix = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }
        int[] newBase = base.clone();
        int[] newNonBase = nonBase.clone();

        return new Tableau(newMatrix, newBase, newNonBase);
    }

}
