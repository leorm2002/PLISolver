package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.Fraction;
import it.naddeil.ro.common.Pair;


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

    public void solve() {
        while (canImprove()) {
            int pivotColumn = findPivotColumn();
            int pivotRow = findPivotRow(pivotColumn);
            pivot(pivotRow, pivotColumn);
        }
        
        // Potrebbe essere necessario portare in forma canonica
            var basis = getCostoRidottoDaAggiornare();
            while(basis.getFirst() != -1){
            pivot(basis.getFirst(), basis.getSecond());
            basis = getCostoRidottoDaAggiornare();
        }
        
    }

    private boolean canImprove() {
        Fraction[] objectiveRow = tableau[0];
        for (int i = 1; i < objectiveRow.length; i++) {
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
