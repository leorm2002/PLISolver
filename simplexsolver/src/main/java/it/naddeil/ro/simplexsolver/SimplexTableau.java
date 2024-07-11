package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.models.Pair;
import it.naddeil.ro.common.utils.Fraction;
import pabeles.concurrency.IntObjectConsumer;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class SimplexTableau {
    private Fraction[][] tableau;
    public Fraction[][] getTableau() {
		return tableau;
	}

	private int numVariables;
    private int numConstraints;

    public SimplexTableau(Fraction[][] tableau) {
		this.tableau = tableau;
        numVariables = tableau[0].length - 1;
        numConstraints = tableau.length - 1;
	}

	public SimplexTableau(Fraction[] objectiveFunction, Fraction[][] constraints, Fraction[] rightHandSide) {
        numVariables = objectiveFunction.length;
        numConstraints = constraints.length;
        
        // Inizializza il tableau
        tableau = new Fraction[numConstraints + 1][numVariables + 1];
        
        // Aggiungi la funzione obiettivo
        Fraction[] objectiveRow = new Fraction[numVariables + 1];
        for (int i = 0; i < numVariables; i++) {
            objectiveRow[i] = objectiveFunction[i];
        }

        objectiveRow[numVariables]= Fraction.ZERO; // Valore della funzione obiettivo

        tableau[0] = objectiveRow;
        
        // Aggiungi i vincoli
        for (int i = 0; i < numConstraints; i++) {
            Fraction[] constraintRow = new Fraction[numVariables + 1];
            for (int j = 0; j < numVariables; j++) {
                constraintRow[j] = constraints[i][j];
            }
            constraintRow[numVariables] = rightHandSide[i];
            tableau[i +1] = constraintRow;
        }
    }
    public void solve(boolean dueFasi) {
        solve(new ArrayList<>(), false, dueFasi);

    }

    Pair<Integer, Integer> degenere(List<Integer> artificialBasis){
        // Ritornerà true se esiste una base che è una variabile artificiale
        for (Integer col : artificialBasis) {
            if(isBasis(col, tableau)){
                return Pair.of(col, IntStream.range(0, tableau.length).filter(i -> tableau[i][col].equals(Fraction.ONE)).findFirst().getAsInt());
            }
        }
        return Pair.of(-1, -1);
    }

    static void verificaW(Fraction w){
        if(w.compareTo(Fraction.ZERO) != 0){
            throw new RuntimeException("Il valore di W non è 0");
        }
    }

    void gestisciDegenerazione(int colonna, int riga, int numeroVariabiliOrig){
        // Due casi: tutti i coefficienti della riga pivot sono 0 => possiamo eliminare la riga
        Fraction[] rigaTableau = tableau[riga];
        boolean tuttiZero = true;   
        for (int i = 0; i < numeroVariabiliOrig; i++) {
            if(!rigaTableau[i].equals(Fraction.ZERO)){
                tuttiZero = false;
                break;
            }
        }
        if(tuttiZero){
            // Possiamo eliminare vincolo
            Fraction[][] nuovoTableau = new Fraction[tableau.length - 1][tableau[0].length];
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
                if(!rigaTableau[i].equals(Fraction.ZERO) && !isBasis(i, tableau)){
                    colonnaPivot = i;
                    break;
                }
            }
            pivot(riga, colonnaPivot);
        }
    }
    void portaInCanonica(){
        // Potrebbe essere necessario portare in forma canonica
        var basis = getCostoRidottoDaAggiornare();
        while(basis.getFirst() != -1){
            pivot(basis.getFirst(), basis.getSecond());
            basis = getCostoRidottoDaAggiornare();
        }
    }

    public void solve(List<Integer> artificialBasis, boolean fase1, boolean dueFasi) {
        if(dueFasi && !fase1){
            portaInCanonica();
        }
        while (canImprove()) {
            printTableau();
            int pivotColumn = findPivotColumn();
            int pivotRow = findPivotRow(pivotColumn);
            if(pivotRow == -1){
                throw new RuntimeException("Problema illimitato");
            }
            pivot(pivotRow, pivotColumn);

        }
        // Studio W
        if(fase1){
            verificaW(tableau[0][tableau[0].length - 1]);
    
            var deg = degenere(artificialBasis);
            while (deg.getFirst() != -1) {
                gestisciDegenerazione(deg.getFirst(), deg.getSecond(), tableau[0].length - 1 - artificialBasis.size());
                deg = degenere(artificialBasis);
            }
            
            printTableau();
            // Verifica base degenere
            
        }
        portaInCanonica();
        
    }

    private boolean canImprove() {
        Fraction[] objectiveRow = tableau[0];
        for (int i = 0; i < objectiveRow.length - 1; i++) {
            if (objectiveRow[i].compareTo(Fraction.ZERO) < 0) {
                return true;
            }
        }
        return false;
    }

    private int findPivotColumn() {
        Fraction[] objectiveRow = tableau[0];
        for (int i = 0; i < objectiveRow.length; i++) {
            if (objectiveRow[i].compareTo(Fraction.ZERO) < 0) {
                return i;
            }
        }
        return -1;
    }

    private int findPivotRow(int pivotColumn) {
        int pivotRow = -1;
        Fraction minRatio = Fraction.POSITIVE_INFINITY;
        for (int i = 1; i < tableau.length; i++) {
            Fraction[] row = tableau[i];
            if (row[pivotColumn].compareTo(Fraction.ZERO) > 0) {
                Fraction ratio = row[row.length -1].divide(row[pivotColumn]);
                if (ratio.compareTo(minRatio) < 0) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    private void pivot(int pivotRow, int pivotColumn) {
        Fraction[] pivotRowValues = tableau[pivotRow];
        Fraction pivotValue = pivotRowValues[pivotColumn];
        
        // Normalizza la riga pivot
        for (int i = 0; i < pivotRowValues.length; i++) {
            pivotRowValues[i] =  pivotRowValues[i].divide(pivotValue);
        }
        
        // Aggiorna le altre righe
        for (int i = 0; i < tableau.length; i++) {
            if (i != pivotRow) {
                Fraction[] row = tableau[i];
                Fraction multiplier = row[pivotColumn];
                for (int j = 0; j < row.length; j++) {
                    Fraction newValue = row[j].subtract(multiplier.multiply(pivotRowValues[j]));
                    row[j] =newValue;
                }
            }
        }
    }

    public void printTableau() {
        printTableau(tableau);
    }
    public static void printTableau(Fraction[][] tableau) {
        for (Fraction[] row : tableau) {
            for (Fraction value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Fraction getObjectiveValue() {
        return tableau[0][0];
    }

    public static boolean isBasis(int column, Fraction[][] tableau){
        boolean basicVariable = false;
        int basicRow = -1;
        for (int row = 1; row < tableau.length; row++) {
            if (tableau[row][column].equals(Fraction.ONE)) {
                if (basicRow == -1) {
                    basicRow = row;
                    basicVariable = true;
                    
                } else {
                    basicVariable = false;
                    break;
                }
            } else if (!tableau[row][column].equals(Fraction.ZERO)) {
                basicVariable = false;
                break;
            }
        }
        return basicVariable;
    }

    public static List<Fraction[]> getBasis(Fraction[][] tableau, int numVariables, int numConstraints){
        List<Fraction[]> basis = new ArrayList<>();
        for (int column = 0; column < numVariables; column++) {
            boolean basicVariable = true;
            int basicRow = -1;
            for (int row = 1; row < tableau.length; row++) {
                if (tableau[row][column].equals(Fraction.ONE)) {
                    if (basicRow == -1) {
                        basicRow = row;
                    } else {
                        basicVariable = false;
                        break;
                    }
                } else if (!tableau[row][column].equals(Fraction.ZERO)) {
                    basicVariable = false;
                    break;
                }
            }
            if(basicVariable){
                // Ricostruisco la colonna
                Fraction[] basisColumn = new Fraction[numConstraints];
                for (int i = 0; i < numConstraints; i++) {
                    basisColumn[i] = i == basicRow - 1 ? Fraction.ONE : Fraction.ZERO;
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
                if (tableau[row][column].equals(Fraction.ONE)) {
                    if (basicRow == -1) {
                        basicRow = row;
                    } else {
                        basicVariable = false;
                        break;
                    }
                } else if (!tableau[row][column].equals(Fraction.ZERO)) {
                    basicVariable = false;
                    break;
                }
            }
            if(basicVariable && !tableau[0][column].equals(Fraction.ZERO)){
                // Controlla se il costo ridotto è 0
                return Pair.of(basicRow, column);
            }
        }
        return Pair.of(-1, -1);
    }
}
