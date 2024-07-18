package it.naddeil.ro.simplexsolver;



import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ejml.interfaces.linsol.LinearSolver;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.models.Pair;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;

import java.util.ArrayList;

public class DueFasi {
    /**
     * InnerDueFasi
     */
    public record InnerDueFasi(Comp[][] tableau, Comp[][] identity, boolean needed, List<Message> passaggi) {
    }

    static Comp[][] getIdentity(int n){
        Comp[][] identity = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                identity[i][j] = i == j ? Comp.ONE : Comp.ZERO;
            }
        }
        return identity;
    }

    static Comp[][] transpose(Comp[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length;
        Comp[][] transposed = new Comp[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++){
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    static Comp[] getExtendedObjective(Comp[] objectiveFunction, int numConstraints){
        int numVariables = objectiveFunction.length;
        Comp[] extendedObjective = new Comp[numVariables + numConstraints];
        for (int i = 0; i < numVariables - 1; i++) {
            extendedObjective[i] = Comp.ZERO;
        }
        for (int i = numVariables - 1; i < numVariables + numConstraints; i++) {
            extendedObjective[i] = Comp.ONE;
        }
        extendedObjective[extendedObjective.length - 1] = objectiveFunction[objectiveFunction.length - 1];
        return extendedObjective;
    }

    static boolean equalsFrac(Comp[] a, Comp[] b){
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

    static boolean contains(List<Comp[]> list, Comp[] element){
        for (Comp[] fractions : list) {
            if (equalsFrac(fractions,element)) {
                return true;
            }
        }
        return false;
    }

    static Comp[][] getAdditionalBasis(int numConstraints, List<Comp[]> basiCorrenti){
        Comp[][] identity = getIdentity(numConstraints);

        Comp[][] iT = transpose(identity);

        Comp[][] out = new Comp[numConstraints - basiCorrenti.size()][numConstraints];
        int i = 0;
        for (Comp[] fractions : iT) {
            if (!contains(basiCorrenti,fractions)) {
                out[i] = fractions;
                i += 1;
            }
        }

        return transpose(out);
    }

    static List<Comp[]> removeDup(List<Comp[]> list){
        List<Comp[]> ret = new ArrayList<>();

        for (Comp[] base : list) {
            if (!contains(ret,base)) {
                ret.add(base);
            }
        }
    
        return ret;
    }

    static InnerDueFasi aggiungiVariabiliSintetiche(Comp[][] tableau){
        int numConstraints = tableau.length - 1;
        int numVariables = tableau[0].length - 1;
        List<Comp[]> basiCorrenti = removeDup(SimplexTableau.getBasis(tableau, numVariables, numConstraints));
        int nbasi = basiCorrenti.size();
        if(numConstraints == nbasi ){
            return new InnerDueFasi(null, null, false, Collections.emptyList());
        }
        int nuovoNumeroVariabili = numVariables + numConstraints - basiCorrenti.size();
        int numeroBasiAggiunte = nuovoNumeroVariabili - numVariables;
        Comp[][] nuovoTableau = new Comp[numConstraints + 1][nuovoNumeroVariabili + 1];
        Comp[][] identity = getAdditionalBasis(numConstraints, basiCorrenti);
        // Riporto funzione obbiettivo artificiale
        nuovoTableau[0] = getExtendedObjective(tableau[0], numConstraints - basiCorrenti.size());
        // Riporto i vincoli
        for (int i = 1; i < numConstraints + 1; i++) {
            Comp[] newRow = new Comp[nuovoNumeroVariabili + 1];
            for (int j = 0; j < numVariables; j++) {
                newRow[j] = tableau[i][j];
            }
            Comp[] identityRow = identity[i - 1];
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

    static Comp[][] pulisciTableau(Comp[][] ottimo, Comp[] fObbOrig){
        int numeroVariabiliOriginali = fObbOrig.length - 1;
        Comp[][] nuovoTableau = new Comp[ottimo.length][numeroVariabiliOriginali + 1];
        for (int i = 0; i < ottimo.length; i++) {
            Comp[] newRow = new Comp[numeroVariabiliOriginali + 1];
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

    static Comp[] sub(Comp[] a, Comp[] b){
        Comp[] c = new Comp[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i].subtract(b[i]);
        }
        return c;
    }

    static boolean contains(Comp[] a, Comp b){
        for (Comp fraction : a) {
            if (fraction.equals(b)) {
                return true;
            }
        }
        return false;
    }
    static void primaOperazione(Comp[][] tableau, Comp[][] basiSintetiche){
        List<Integer> righeDaSottrarre = new ArrayList<>();
        for(int i= 0; i < basiSintetiche.length; i++){
            if (contains(basiSintetiche[i], Comp.ONE)) {
                righeDaSottrarre.add(i);
            }
        }
        for (Integer i : righeDaSottrarre) {
            tableau[0] = sub(tableau[0], tableau[i +1]);
        }
    }

    static List<Integer> getAVIndexes(Comp[][] row, int numberOfVariables){
        if(row.length == 0) return new ArrayList<>();

        return IntStream.range(0, row[0].length).map(i -> i + numberOfVariables).boxed().collect(Collectors.toList());
    }

    static Comp[][] applicaMetodoDueFasi(Comp[][] tableau, List<Integer> numeroVariabiliSlack){
        return applicaMetodoDueFasi(tableau, numeroVariabiliSlack, new ArrayList<>());
    }
    static Comp[][] applicaMetodoDueFasi(Comp[][] tableau, List<Integer> numeroVariabiliSlack, List<Message> passaggi){
        InnerDueFasi nuovoTableau = aggiungiVariabiliSintetiche(tableau);
        final Comp[][] tab;
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
