package it.naddeil.ro.api;

public class MatrixUtils {

    /**
     * Moltiplica una riga di una matrice per uno scalare
     */
    public static double[][] multiplyRow(double[][] matrix, int row, double scalar) {
        double[] newRow = new double[matrix[row].length];
        for (int i = 0; i < matrix[row].length; i++) {
            newRow[i] = matrix[row][i] * scalar;
        }
        matrix[row] = newRow;
        return matrix;
    }

    /**
     * Somma due righe di una matrice moltiplicando la seconda per uno scalare
     */
    public static double[][] addRow(double[][] matrix, int row1, int daSottrarre, double scalar) {
        double[] newRow = new double[matrix[row1].length];
        for (int i = 0; i < matrix[row1].length; i++) {
            newRow[i] = matrix[row1][i] + matrix[daSottrarre][i] * scalar;
        }
        matrix[row1] = newRow;
        return matrix;
    }


    public static double[][] transpose(double[][] matrix) {
        double[][] transposed = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

}
