package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.api.MatrixUtils;

public class CreatoreDuale {

    static double[][] creaDuale(double[][] problemaFormaStandard) {
        // Input problema nella forma:
        // min c^T x
        // Ax = b
        // ovvero
        // 0 c c c
        // b a a a
        // b a a a

        // Lo converto in:
        // max b^T y
        // A^T y <= c
        // ovvero
        // 0 b b
        // c a a
        // c a a
        // c a a

        return MatrixUtils.transpose(problemaFormaStandard);
    }
}
