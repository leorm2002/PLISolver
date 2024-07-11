package it.naddeil.ro.common.utils;

public class TableauUtils {
    public static void printTableau(Fraction[][] tableau) {
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                System.out.print(tableau[i][j] + " ");
            }
            System.out.println();
        }
    }
}
