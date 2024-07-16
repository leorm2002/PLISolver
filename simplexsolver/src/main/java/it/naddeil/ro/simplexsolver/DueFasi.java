package it.naddeil.ro.simplexsolver;



import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ejml.interfaces.linsol.LinearSolver;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.models.Pair;
import it.naddeil.ro.common.utils.Fraction;

import java.util.ArrayList;

public class DueFasi {
    /**
     * InnerDueFasi
     */
    public record InnerDueFasi(Fraction[][] tableau, Fraction[][] identity, boolean needed, List<Message> passaggi) {
    }

    static Fraction[][] getIdentity(int n){
        Fraction[][] identity = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                identity[i][j] = i == j ? Fraction.ONE : Fraction.ZERO;
            }
        }
        return identity;
    }

    static Fraction[][] transpose(Fraction[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length;
        Fraction[][] transposed = new Fraction[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++){
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
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

    static boolean equalsFrac(Fraction[] a, Fraction[] b){
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

    static boolean contains(List<Fraction[]> list, Fraction[] element){
        for (Fraction[] fractions : list) {
            if (equalsFrac(fractions,element)) {
                return true;
            }
        }
        return false;
    }

    static Fraction[][] getAdditionalBasis(int numConstraints, List<Fraction[]> basiCorrenti){
        Fraction[][] identity = getIdentity(numConstraints);

        Fraction[][] iT = transpose(identity);

        Fraction[][] out = new Fraction[numConstraints - basiCorrenti.size()][numConstraints];
        int i = 0;
        for (Fraction[] fractions : iT) {
            if (!contains(basiCorrenti,fractions)) {
                out[i] = fractions;
                i += 1;
            }
        }

        return transpose(out);
    }

    static List<Fraction[]> removeDup(List<Fraction[]> list){
        List<Fraction[]> ret = new ArrayList<>();

        for (Fraction[] base : list) {
            if (!contains(ret,base)) {
                ret.add(base);
            }
        }
    
        return ret;
    }

    static InnerDueFasi aggiungiVariabiliSintetiche(Fraction[][] tableau){
        int numConstraints = tableau.length - 1;
        int numVariables = tableau[0].length - 1;
        List<Fraction[]> basiCorrenti = removeDup(SimplexTableau.getBasis(tableau, numVariables, numConstraints));
        int nbasi = basiCorrenti.size();
        if(numConstraints == nbasi ){
            return new InnerDueFasi(null, null, false, Collections.emptyList());
        }
        int nuovoNumeroVariabili = numVariables + numConstraints - basiCorrenti.size();
        int numeroBasiAggiunte = nuovoNumeroVariabili - numVariables;
        Fraction[][] nuovoTableau = new Fraction[numConstraints + 1][nuovoNumeroVariabili + 1];
        Fraction[][] identity = getAdditionalBasis(numConstraints, basiCorrenti);
        // Riporto funzione obbiettivo artificiale
        nuovoTableau[0] = getExtendedObjective(tableau[0], numConstraints - basiCorrenti.size());
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
        Message msg = Message.messaggioSemplice("Il problema non ha una base iniziale ammissibile, aggiungo " + numeroBasiAggiunte + " variabili sintetiche");
        Message msg2 = Message.messaggioConTableau("Tableau prima dell'aggiunta", tableau);
        Message msg3 = Message.messaggioConTableau("Tableau dopo aggiunta", nuovoTableau);


        return new InnerDueFasi(nuovoTableau, identity, true, List.of(msg, msg2, msg3));
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

    static boolean contains(Fraction[] a, Fraction b){
        for (Fraction fraction : a) {
            if (fraction.equals(b)) {
                return true;
            }
        }
        return false;
    }
    static void primaOperazione(Fraction[][] tableau, Fraction[][] basiSintetiche){
        List<Integer> righeDaSottrarre = new ArrayList<>();
        for(int i= 0; i < basiSintetiche.length; i++){
            if (contains(basiSintetiche[i], Fraction.ONE)) {
                righeDaSottrarre.add(i);
            }
        }
        for (Integer i : righeDaSottrarre) {
            tableau[0] = sub(tableau[0], tableau[i +1]);
        }
    }

    static List<Integer> getAVIndexes(Fraction[][] row, int numberOfVariables){
        if(row.length == 0) return new ArrayList<>();

        return IntStream.range(0, row[0].length).map(i -> i + numberOfVariables).boxed().collect(Collectors.toList());
    }

    static Fraction[][] applicaMetodoDueFasi(Fraction[][] tableau, List<Integer> numeroVariabiliSlack){
        return applicaMetodoDueFasi(tableau, numeroVariabiliSlack, new ArrayList<>());
    }
    static Fraction[][] applicaMetodoDueFasi(Fraction[][] tableau, List<Integer> numeroVariabiliSlack, List<Message> passaggi){
        InnerDueFasi nuovoTableau = aggiungiVariabiliSintetiche(tableau);
        final Fraction[][] tab;
        passaggi.addAll(nuovoTableau.passaggi);
        if(nuovoTableau.needed){
            passaggi.add(Message.messaggioSemplice("Risolvo il problema con le variabili sintetiche, vado ad azzerare i costi ridotti delle variabili sintetiche"));
            primaOperazione(nuovoTableau.tableau, nuovoTableau.identity);
            passaggi.add(Message.messaggioConTableau("Tableau una volta effettuato azzeramento", nuovoTableau.tableau));
            SimplexTableau st = new SimplexTableau(nuovoTableau.tableau);
            st.solve(getAVIndexes(nuovoTableau.identity, tableau[0].length - 1),true);
            passaggi.addAll(st.getPassaggi());
            passaggi.add(Message.messaggioConTableau("Tableu ottimo della fase 1", st.getTableau()));

            tab = pulisciTableau(st.getTableau(), tableau[0]);
            passaggi.add(Message.messaggioConTableau("Tableu ottimo finale della fase 1 con variabili rimosse, riolvo con il simplesso", tab));
        }else{
            tab = tableau;
        }

        SimplexTableau st2 = new SimplexTableau(tab);
        st2.solve(numeroVariabiliSlack, false);
        passaggi.addAll(st2.getPassaggi());

        return st2.getTableau();
    }
}
