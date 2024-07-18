package it.naddeil.ro.apachesimplexsolver;


import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;

public class MatrixBridge {
    static Comp[][] transform(double[][] m){
        Comp[][] result = new Comp[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                result[i][j] = Fraction.of(m[i][j]);
            }
        }
        return result;
    }
}
