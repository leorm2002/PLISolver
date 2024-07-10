package it.naddeil.ro.simplexsolver;


import org.ejml.equation.Function;

import it.naddeil.ro.common.Fraction;

public class DueFasi {

    static Fraction[][] getIdentity(int n){
        Fraction[][] identity = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                identity[i][j] = i == j ? Fraction.ONE : Fraction.ZERO;
            }
        }
        return identity;
    }
    static Fraction[] getExtendedObjective(Fraction[] objectiveFunction, int numConstraints){
        int numVariables = objectiveFunction.length;
        Fraction[] extendedObjective = new Fraction[numVariables + numConstraints];
        for (int i = 0; i < numVariables - 1; i++) {
            extendedObjective[i] = Fraction.ZERO;
        }
        for (int i = numVariables - 1; i < numVariables + numConstraints; i++) {
            extendedObjective[i] = Fraction.ONE;
        }
        extendedObjective[extendedObjective.length - 1] = objectiveFunction[objectiveFunction.length - 1];
        return extendedObjective;
    }


    static Fraction[][] aggiungiVariabiliSintetiche(Fraction[][] tableau){
        int numConstraints = tableau.length - 1;
        int numVariables = tableau[0].length - 1;
        int nuovoNumeroVariabili = numVariables + numConstraints;
        Fraction[][] nuovoTableau = new Fraction[numConstraints + 1][nuovoNumeroVariabili + 1];
        Fraction[][] identity = getIdentity(numConstraints);
        // Riporto funzione obbiettivo artificiale
        nuovoTableau[0] = getExtendedObjective(tableau[0], numConstraints);
        // Riporto i vincoli
        for (int i = 1; i < numConstraints + 1; i++) {
            Fraction[] newRow = new Fraction[nuovoNumeroVariabili + 1];
            for (int j = 0; j < numVariables; j++) {
                newRow[j] = tableau[i][j];
            }
            Fraction[] identityRow = identity[i - 1];
            for (int j = numVariables; j < nuovoNumeroVariabili; j++) {
                newRow[j] = identityRow[j - numVariables];
            }

            newRow[nuovoNumeroVariabili] = tableau[i][numVariables];
            nuovoTableau[i] = newRow;
        }

        return nuovoTableau;
    }

    static Fraction[][] pulisciTableau(Fraction[][] ottimo, Fraction[] fObbOrig){
        int numeroVariabiliOriginali = fObbOrig.length - 1;
        Fraction[][] nuovoTableau = new Fraction[ottimo.length][numeroVariabiliOriginali + 1];
        for (int i = 0; i < ottimo.length; i++) {
            Fraction[] newRow = new Fraction[numeroVariabiliOriginali + 1];
            for (int j = 0; j < numeroVariabiliOriginali; j++) {
                newRow[j] = ottimo[i][j];
            }
            newRow[numeroVariabiliOriginali] = ottimo[i][ottimo[i].length - 1];
            nuovoTableau[i] = newRow;
        }
        // RIpristino funzione obbiettivo
        for (int i = 0; i < fObbOrig.length; i++) {
            nuovoTableau[0][i] = fObbOrig[i];
        }

        return nuovoTableau;
    }

    static Fraction[] sub(Fraction[] a, Fraction[] b){
        Fraction[] c = new Fraction[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i].subtract(b[i]);
        }
        return c;
    }

    static void primaOperazione(Fraction[][] tableau){
        for (int i = 1; i < tableau.length; i++) {
            tableau[0] = sub(tableau[0], tableau[i]);
        }
    }

    static Fraction[][] applicaMetodoDueFasi(Fraction[][] tableau){
        Fraction[][] nuovoTableau = aggiungiVariabiliSintetiche(tableau);
        primaOperazione(nuovoTableau);
        SimplexTableau st = new SimplexTableau(nuovoTableau);
        st.solve();
        var tab = pulisciTableau(st.getTableau(), tableau[0]);
        SimplexTableau st2 = new SimplexTableau(tab);
        st2.solve();
        return st2.getTableau();
    }
}
