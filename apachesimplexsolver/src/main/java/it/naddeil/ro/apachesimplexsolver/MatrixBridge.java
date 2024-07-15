package it.naddeil.ro.apachesimplexsolver;


import it.naddeil.ro.common.utils.Fraction;

public class MatrixBridge {
    static Fraction[][] transform(double[][] m){
        Fraction[][] result = new Fraction[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                result[i][j] = Fraction.of(m[i][j]);
            }
        }
        return result;
    }
}
