package it.naddeil.ro.api;

public class GaussianSolver {
    private GaussianSolver() {
    }

    // public static int N = 3; // Number of unknowns

    // function to get matrix content
    public static double[] gaussianElimination(double[][] mat, int n) {

        /* reduction into r.e.f. */
        int singularFlag = forwardElim(mat, n);

        /* if matrix is singular */
        if (singularFlag != -1) {
            System.out.println("Singular Matrix.");

            /*
             * if the RHS of equation corresponding to zero row is 0, * system has infinitely many solutions, else inconsistent
             */
            if (mat[singularFlag][n] != 0) {
                System.out.print("Inconsistent System.");
            } else {
                System.out.print("May have infinitely many solutions.");
            }

            throw new IllegalStateException("Singular Matrix");
        }

        /*
         * get solution to system and print it using backward substitution
         */
        return backSub(mat, n);
    }

    // function for elementary operation of swapping two
    // rows
    static void swapRow(double[][] mat, int i, int j, int n) {
        for (int k = 0; k <= n; k++) {
            double temp = mat[i][k];
            mat[i][k] = mat[j][k];
            mat[j][k] = temp;
        }
    }

    // function to print matrix content at any stage
    static void print(double[][] mat, int n) {
        for (int i = 0; i < n; i++, System.out.println())
            for (int j = 0; j <= n; j++)
                System.out.print(mat[i][j]);
        System.out.println();
    }

    // function to reduce matrix to r.e.f.
    static int forwardElim(double[][] mat, int n) {
        for (int k = 0; k < n; k++) {

            // Initialize maximum value and index for pivot
            int iMax = k;
            int vMax = (int) mat[iMax][k];

            /* find greater amplitude for pivot if any */
            for (int i = k + 1; i < n; i++)
                if (Math.abs(mat[i][k]) > vMax) {
                    vMax = (int) mat[i][k];
                    iMax = i;
                }

            /*
             * if a principal diagonal element is zero, it denotes that matrix is singular, and will lead to a division-by-zero later.
             */
            if (mat[k][iMax] == 0)
                return k; // Matrix is singular

            /*
             * Swap the greatest value row with current row
             */
            if (iMax != k)
                swapRow(mat, k, iMax, n);

            for (int i = k + 1; i < n; i++) {

                /*
                 * factor f to set current row kth element to 0, and subsequently remaining kth column to 0
                 */
                double f = mat[i][k] / mat[k][k];

                /*
                 * subtract fth multiple of corresponding kth row element
                 */
                for (int j = k + 1; j <= n; j++)
                    mat[i][j] -= mat[k][j] * f;

                /*
                 * filling lower triangular matrix with zeros
                 */
                mat[i][k] = 0;
            }

            // print(mat); //for matrix state
        }

        // print(mat); //for matrix state
        return -1;
    }

    // function to calculate the values of the unknowns
    static double[] backSub(double[][] mat, int n) {
        double[] x = new double[n]; // An array to store solution

        /*
         * Start calculating from last equation up to the first
         */
        for (int i = n - 1; i >= 0; i--) {

            /* start with the RHS of the equation */
            x[i] = mat[i][n];

            /*
             * Initialize j to i+1 since matrix is upper triangular
             */
            for (int j = i + 1; j < n; j++) {

                /*
                 * subtract all the lhs values except the coefficient of the variable whose value is being calculated
                 */
                x[i] -= mat[i][j] * x[j];
            }

            /*
             * divide the RHS by the coefficient of the unknown being calculated
             */
            x[i] = x[i] / mat[i][i];
        }

        return x;
    }

}
