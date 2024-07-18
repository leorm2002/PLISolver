package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.exceptions.ProblemaInizialeIllimitato;
import it.naddeil.ro.common.exceptions.ProblemaInizialeImpossibile;
import it.naddeil.ro.common.models.Pair;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.Collections;

import org.ejml.interfaces.linsol.LinearSolver;

public class SimplexTableau {
    private Comp[][] tableau;
    private List<Message> passaggi = new ArrayList<>();
	private int numVariables;
    private int numConstraints;

    public SimplexTableau(Comp[][] tableau) {
		this.tableau = tableau;
        numVariables = tableau[0].length - 1;
        numConstraints = tableau.length - 1;
	}

	public SimplexTableau(Comp[] objectiveFunction, Comp[][] constraints, Comp[] rightHandSide) {
        numVariables = objectiveFunction.length;
        numConstraints = constraints.length;
        
        // Inizializza il tableau
        tableau = new Comp[numConstraints + 1][numVariables + 1];
        
        // Aggiungi la funzione obiettivo
        Comp[] objectiveRow = new Comp[numVariables + 1];
        for (int i = 0; i < numVariables; i++) {
            objectiveRow[i] = objectiveFunction[i];
        }

        objectiveRow[numVariables]= Comp.ZERO; // Valore della funzione obiettivo

        tableau[0] = objectiveRow;
        
        // Aggiungi i vincoli
        for (int i = 0; i < numConstraints; i++) {
            Comp[] constraintRow = new Comp[numVariables + 1];
            for (int j = 0; j < numVariables; j++) {
                constraintRow[j] = constraints[i][j];
            }
            constraintRow[numVariables] = rightHandSide[i];
            tableau[i +1] = constraintRow;
        }
    }
    public void solve(int numroVariabiliSlack) {
        solve(new ArrayList<>(), false);

    }

    Pair<Integer, Integer> degenere(List<Integer> artificialBasis){
        // Ritornerà true se esiste una base che è una variabile artificiale
        for (Integer col : artificialBasis) {
            if(isBasis(col, tableau)){
                return Pair.of(col, IntStream.range(0, tableau.length).filter(i -> tableau[i][col].equals(Comp.ONE)).findFirst().getAsInt());
            }
        }
        return Pair.of(-1, -1);
    }

    void verificaW(Comp w){
        if(w.compareTo(Comp.ZERO) != 0){
            passaggi.add(Message.messaggioSemplice("W != 0, problema non risolvibile senza uso di variabili ausiliarie"));
            throw new ProblemaInizialeImpossibile(null);
        }
    }

    void gestisciDegenerazione(int colonna, int riga, int numeroVariabiliOrig){
        // Due casi: tutti i coefficienti della riga pivot sono 0 => possiamo eliminare la riga
        Comp[] rigaTableau = tableau[riga];
        boolean tuttiZero = true;   
        for (int i = 0; i < numeroVariabiliOrig; i++) {
            if(!rigaTableau[i].equals(Comp.ZERO)){
                tuttiZero = false;
                break;
            }
        }
        if(tuttiZero){
            // Possiamo eliminare vincolo
            Comp[][] nuovoTableau = new Comp[tableau.length - 1][tableau[0].length];
            for (int i = 0; i < riga; i++) {
                nuovoTableau[i] = tableau[i];
            }
            for (int i = riga + 1; i < tableau.length; i++) {
                nuovoTableau[i - 1] = tableau[i];
            }
            this.tableau = nuovoTableau;
            this.numConstraints -= 1;
        }
        // Almeno un coefficiente è diverso da 0 => possiamo pivotare su quello
        else{
            int colonnaPivot = -1;
            for (int i = 0; i < numeroVariabiliOrig; i++) {
                if(!rigaTableau[i].equals(Comp.ZERO) && !isBasis(i, tableau)){
                    colonnaPivot = i;
                    break;
                }
            }
            pivot(riga, colonnaPivot);
        }
    }

    List<Message> portaInCanonica(){
        List<Message> passaggi = new ArrayList<>();
        boolean shouldReturn = false;
        // Potrebbe essere necessario portare in forma canonica
        passaggi.add(Message.messaggioSemplice("Porto il tableau in forma canonica"));
        var basis = getCostoRidottoDaAggiornare();
        while(basis.getFirst() != -1){
            pivot(basis.getFirst(), basis.getSecond());
            passaggi.add(Message.messaggioSemplice(String.format("Pivot su X%s R%s", basis.getSecond() + 1, basis.getFirst())));
            basis = getCostoRidottoDaAggiornare();
            passaggi.add(
            Message.messaggioConTableau(String.format("Esiste base con costi ridotti positivi (tableu non in forma canonica), pivot su X%s R%s, tableau dopo pivotaggio:",basis.getFirst() + 1,basis.getSecond()), tableau)
            );
            shouldReturn = true;
        }
        passaggi.add(Message.messaggioConTableau("Tableau portato in forma canonica", tableau));

        return shouldReturn ? passaggi : Collections.emptyList();
    }

    public void solve(List<Integer> artificialBasis, boolean fase1) {
        System.out.println("Tableau iniziale:");
        printTableau();

        System.out.println();
        passaggi.addAll(portaInCanonica());
        while (canImprove()) {
            int pivotColumn = findPivotColumn();
            int pivotRow = findPivotRow(pivotColumn);
            if(pivotRow == -1){
                passaggi.add(Message.messaggioSemplice("Il problema è illimitato"));
                throw new ProblemaInizialeIllimitato(null);
            }
            passaggi.add(Message.messaggioConTableau(String.format("La soluzione può essere migliorata, faccio pivot su x%s r%s", pivotRow + 1, pivotColumn), tableau));
            pivot(pivotRow, pivotColumn);
            System.out.println("Tableau:");
            passaggi.add(Message.messaggioConTableau("Tableau dopo il pivot", tableau));

            printTableau();

            System.out.println();
        }
        // Studio W
        if(fase1){
            verificaW(tableau[0][tableau[0].length - 1]);
        }
        if(!artificialBasis.isEmpty()){
            boolean degenere = false;
            List<Message> passiDeg = new ArrayList<>();
            var deg = degenere(artificialBasis);
            while (deg.getFirst() != -1) {
                gestisciDegenerazione(deg.getFirst(), deg.getSecond(), tableau[0].length - 1 - artificialBasis.size());
                deg = degenere(artificialBasis);
                degenere = true;
            }
            if(degenere){
                passaggi.add(Message.messaggioSemplice("Il tableau è degenere, alcune variabili artificiali sono nella base, eseguo pivoting per riportare su variabili originali"));
            }
        }
        passaggi.addAll(portaInCanonica());
        //if(canImprove()){
            //solve(artificialBasis, true);
        //}
        System.out.println("Tableau finale:");
        printTableau();
        System.out.println();
        passaggi.add(Message.messaggioConTableau("Tableau finale ottimo", tableau));
        
    }

    private boolean canImprove() {
        Comp[] objectiveRow = tableau[0];
        for (int i = 0; i < objectiveRow.length - 1; i++) {
            if (objectiveRow[i].compareTo(Comp.ZERO) < 0) {
                return true;
            }
        }
        return false;
    }

    private int findPivotColumn() {
        Comp[] objectiveRow = tableau[0];
        for (int i = 0; i < objectiveRow.length; i++) {
            if (objectiveRow[i].compareTo(Comp.ZERO) < 0) {
                return i;
            }
        }
        return -1;
    }

    private int findPivotRow(int pivotColumn) {
        int pivotRow = -1;
        Comp minRatio = Comp.POSITIVE_INFINITY;
        for (int i = 1; i < tableau.length; i++) {
            Comp[] row = tableau[i];
            if (row[pivotColumn].compareTo(Comp.ZERO) > 0) {
                Comp ratio = row[row.length -1].divide(row[pivotColumn]);
                if (ratio.compareTo(minRatio) < 0) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    private void pivot(int pivotRow, int pivotColumn) {
        Comp[] pivotRowValues = tableau[pivotRow];
        Comp pivotValue = pivotRowValues[pivotColumn];
        
        // Normalizza la riga pivot
        for (int i = 0; i < pivotRowValues.length; i++) {
            pivotRowValues[i] =  pivotRowValues[i].divide(pivotValue);
        }
        
        // Aggiorna le altre righe
        for (int i = 0; i < tableau.length; i++) {
            if (i != pivotRow) {
                Comp[] row = tableau[i];
                Comp multiplier = row[pivotColumn];
                for (int j = 0; j < row.length; j++) {
                    Comp newValue = row[j].subtract(multiplier.multiply(pivotRowValues[j]));
                    row[j] =newValue;
                }
            }
        }
    }

    public void printTableau() {
        printTableau(tableau);
    }
    public static void printTableau(Comp[][] tableau) {
        for (Comp[] row : tableau) {
            for (Comp value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Comp getObjectiveValue() {
        return tableau[0][0];
    }

    public static boolean isBasis(int column, Comp[][] tableau){
        boolean basicVariable = false;
        int basicRow = -1;
        for (int row = 1; row < tableau.length; row++) {
            if (tableau[row][column].equals(Comp.ONE)) {
                if (basicRow == -1) {
                    basicRow = row;
                    basicVariable = true;
                } else {
                    basicVariable = false;
                    break;
                }
            } else if (!tableau[row][column].equals(Comp.ZERO)) {
                basicVariable = false;
                break;
            }
        }
        return basicVariable;
    }

    public static List<Comp[]> getBasis(Comp[][] tableau, int numVariables, int numConstraints){
        List<Comp[]> basis = new ArrayList<>();
        for (int column = 0; column < numVariables; column++) {
            boolean basicVariable = true;
            int basicRow = -1;
            for (int row = 1; row < tableau.length; row++) {
                if (tableau[row][column].equals(Comp.ONE)) {
                    if (basicRow == -1) {
                        basicRow = row;
                    } else {
                        basicVariable = false;
                        break;
                    }
                } else if (!tableau[row][column].equals(Comp.ZERO)) {
                    basicVariable = false;
                    break;
                }
            }
            if(basicVariable){
                // Ricostruisco la colonna
                Comp[] basisColumn = new Comp[numConstraints];
                for (int i = 0; i < numConstraints; i++) {
                    basisColumn[i] = i == basicRow - 1 ? Comp.ONE : Comp.ZERO;
                }
                basis.add(basisColumn);
            }
        }
        return basis;
    }

    public Pair<Integer,Integer> getCostoRidottoDaAggiornare() {
        for (int column = 0; column < numVariables; column++) {
            boolean basicVariable = true;
            int basicRow = -1;
            for (int row = 1; row < tableau.length; row++) {
                if (tableau[row][column].equals(Comp.ONE)) {
                    if (basicRow == -1) {
                        basicRow = row;
                    } else {
                        basicVariable = false;
                        break;
                    }
                } else if (!tableau[row][column].equals(Comp.ZERO)) {
                    basicVariable = false;
                    break;
                }
            }
            if(basicVariable && !tableau[0][column].equals(Comp.ZERO)){
                // Controlla se il costo ridotto è 0
                return Pair.of(basicRow, column);
            }
        }
        return Pair.of(-1, -1);
    }

    public List<Message> getPassaggi() {
        return passaggi;
    }

    public Comp[][] getTableau() {
		return tableau;
	}


}
