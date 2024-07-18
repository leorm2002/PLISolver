package it.naddeil.ro.common.utils;

import java.util.ArrayList;
import java.util.List;

public class TableauUtils {
    public static void printTableau(Value[][] tableau) {
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                System.out.print(tableau[i][j] + " ");
            }
            System.out.println();
        }
    }

    
    public static List<Value> getSolution(int numVariables, int numConstraints, Value[][] tableau) {
        List<Value> soluzione = new ArrayList<>();
        for (int j = 0; j < numVariables; j++) {
            Value value = Value.ZERO;
            for (int i = 1; i <= numConstraints; i++) {
                if (isUnitVector(j, numConstraints, tableau) && tableau[i][j].equals(Value.ONE)) {
                    value = tableau[i][numVariables];
                    break;
                }
            }
            soluzione.add(value);
        }
        return soluzione;
    }

    public static boolean isUnitVector(int col, int numConstraints, Value[][] tableau) {
        int oneCount = 0;
        for (int i = 1; i <= numConstraints; i++) {
            if (tableau[i][col].equals(Value.ONE)) {
                oneCount++;
            } else if (!tableau[i][col].equals(Value.ZERO)) {
                return false;
            }
        }
        return oneCount == 1;
    }
}
