package it.naddeil.ro.common.utils;

import java.util.ArrayList;
import java.util.List;

public class TableauUtils {
    public static void printTableau(Fraction[][] tableau) {
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                System.out.print(tableau[i][j] + " ");
            }
            System.out.println();
        }
    }

    
    public static List<Fraction> getSolution(int numVariables, int numConstraints, Fraction[][] tableau) {
        List<Fraction> soluzione = new ArrayList<>();
        for (int j = 0; j < numVariables; j++) {
            Fraction value = Fraction.ZERO;
            for (int i = 1; i <= numConstraints; i++) {
                if (isUnitVector(j, numConstraints, tableau) && tableau[i][j].equals(Fraction.ONE)) {
                    value = tableau[i][numVariables];
                    break;
                }
            }
            soluzione.add(value);
        }
        return soluzione;
    }

    public static boolean isUnitVector(int col, int numConstraints, Fraction[][] tableau) {
        int oneCount = 0;
        for (int i = 1; i <= numConstraints; i++) {
            if (tableau[i][col].equals(Fraction.ONE)) {
                oneCount++;
            } else if (!tableau[i][col].equals(Fraction.ZERO)) {
                return false;
            }
        }
        return oneCount == 1;
    }
}
