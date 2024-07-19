package it.naddeil.ro.simplexsolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.exceptions.ProblemaInizialeIllimitato;
import it.naddeil.ro.common.exceptions.ProblemaInizialeImpossibile;
import it.naddeil.ro.common.models.Pair;
import it.naddeil.ro.common.utils.Value;

public class SimplexTableau {
    private Value[][] tableau;
    private List<Message> passaggi = new ArrayList<>();
    private int numVariables;
    private int numConstraints;

    public SimplexTableau(Value[][] tableau) {
        this.tableau = tableau;
        numVariables = tableau[0].length - 1;
        numConstraints = tableau.length - 1;
    }

    public SimplexTableau(Value[] objectiveFunction, Value[][] constraints, Value[] rightHandSide) {
        numVariables = objectiveFunction.length;
        numConstraints = constraints.length;

        // Inizializza il tableau
        tableau = new Value[numConstraints + 1][numVariables + 1];

        // Aggiungi la funzione obiettivo
        Value[] objectiveRow = new Value[numVariables + 1];
        for (int i = 0; i < numVariables; i++) {
            objectiveRow[i] = objectiveFunction[i];
        }

        objectiveRow[numVariables] = Value.ZERO; // Valore della funzione obiettivo

        tableau[0] = objectiveRow;

        // Aggiungi i vincoli
        for (int i = 0; i < numConstraints; i++) {
            Value[] constraintRow = new Value[numVariables + 1];
            for (int j = 0; j < numVariables; j++) {
                constraintRow[j] = constraints[i][j];
            }
            constraintRow[numVariables] = rightHandSide[i];
            tableau[i + 1] = constraintRow;
        }
    }

    public void solve(int numroVariabiliSlack) {
        solve(new ArrayList<>(), false);

    }

    Pair<Integer, Integer> degenere(List<Integer> artificialBasis) {
        // Ritornerà true se esiste una base che è una variabile artificiale
        for (Integer col : artificialBasis) {
            if (isBasis(col, tableau)) {
                return Pair.of(col, IntStream.range(0, tableau.length).filter(i -> tableau[i][col].equals(Value.ONE)).findFirst().getAsInt());
            }
        }
        return Pair.of(-1, -1);
    }

    void verificaW(Value w) {
        if (w.compareTo(Value.ZERO) != 0) {
            passaggi.add(Message.messaggioSemplice("W != 0, problema non risolvibile senza uso di variabili ausiliarie"));
            throw new ProblemaInizialeImpossibile(passaggi);
        }
    }

    boolean gestisciDegenerazione(int colonna, int riga, int numeroVariabiliOrig, boolean fase1, List<Message> passaggi) {
        // Due casi: tutti i coefficienti della riga pivot sono 0 => possiamo eliminare la riga
        Value[] rigaTableau = tableau[riga];

        boolean tuttiZero = true;
        for (int i = 0; i < numeroVariabiliOrig; i++) {
            if (!rigaTableau[i].equals(Value.ZERO)) {
                tuttiZero = false;
                break;
            }
        }
        if (fase1) {
            if (tuttiZero) {
                passaggi.add(Message.messaggioSemplice(String.format("Riga R%s con tutti i coefficienti del problema originale nulli, eliminata", riga)));
                // Possiamo eliminare vincolo
                Value[][] nuovoTableau = new Value[tableau.length - 1][tableau[0].length];
                for (int i = 0; i < riga; i++) {
                    nuovoTableau[i] = tableau[i];
                }
                for (int i = riga + 1; i < tableau.length; i++) {
                    nuovoTableau[i - 1] = tableau[i];
                }
                this.tableau = nuovoTableau;
                this.numConstraints -= 1;
            }
        }
        // Almeno un coefficiente è diverso da 0 => possiamo pivotare su quello
        if (!tuttiZero) {
            int colonnaPivot = -1;
            for (int i = 0; i < numeroVariabiliOrig; i++) {
                if (!rigaTableau[i].equals(Value.ZERO) && !isBasis(i, tableau)) {
                    colonnaPivot = i;
                    break;
                }
            }
            if (colonnaPivot == -1) {
                return true;
            }
            passaggi.add(Message.messaggioSemplice(String.format("Riga R%s con almeno un coefficiente non nullo, pivot su X%s", riga, colonnaPivot + 1)));
            pivot(riga, colonnaPivot);
        }
        return false;
    }

    List<Message> portaInCanonica() {
        List<Message> passaggi = new ArrayList<>();
        boolean shouldReturn = false;
        // Potrebbe essere necessario portare in forma canonica
        passaggi.add(Message.messaggioSemplice("Porto il tableau in forma canonica"));
        var basis = getCostoRidottoDaAggiornare();
        while (basis.getFirst() != -1) {
            pivot(basis.getFirst(), basis.getSecond());
            passaggi.add(Message.messaggioSemplice(String.format("Pivot su X%s R%s", basis.getSecond() + 1, basis.getFirst())));
            basis = getCostoRidottoDaAggiornare();
            passaggi.add(Message.messaggioConTableau(
                    String.format("Esiste base con costi ridotti positivi (tableu non in forma canonica), pivot su X%s R%s, tableau dopo pivotaggio:",
                            basis.getFirst() + 1, basis.getSecond()),
                    tableau));
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
            if (pivotRow == -1) {
                passaggi.add(Message.messaggioSemplice("Il problema è illimitato"));
                throw new ProblemaInizialeIllimitato(passaggi);
            }
            passaggi.add(Message.messaggioConTableau(String.format("La soluzione può essere migliorata, faccio pivot su x%s r%s", pivotRow + 1, pivotColumn),
                    tableau));
            pivot(pivotRow, pivotColumn);
            System.out.println("Tableau:");
            passaggi.add(Message.messaggioConTableau("Tableau dopo il pivot", tableau));

            printTableau();

            System.out.println();
        }
        // Studio W
        if (fase1) {
            verificaW(tableau[0][tableau[0].length - 1]);
        }
        if (!artificialBasis.isEmpty()) {
            boolean degenere = false;
            List<Message> passiDeg = new ArrayList<>();
            passiDeg.add(Message.messaggioSemplice(
                    "Il tableau è degenere, alcune variabili artificiali sono nella base, eseguo pivoting per riportare su variabili originali"));

            Pair<Integer, Integer> deg = degenere(artificialBasis);
            Pair<Integer, Integer> last = null;
            boolean exit = false;
            while (deg.getFirst() != -1 && !exit) {
                exit = gestisciDegenerazione(deg.getFirst(), deg.getSecond(), tableau[0].length - 1 - artificialBasis.size(), fase1, passiDeg);
                last = deg;
                deg = degenere(artificialBasis);
                degenere = true;
                if (last != null && deg.equals(last)) {
                    break;
                }
            }
            if (degenere) {
                passaggi.addAll(passiDeg);
            }
        }
        passaggi.addAll(portaInCanonica());

        System.out.println("Tableau finale:");
        printTableau();
        System.out.println();
        passaggi.add(Message.messaggioConTableau("Tableau finale ottimo", tableau));

    }

    private boolean canImprove() {
        Value[] objectiveRow = tableau[0];
        for (int i = 0; i < objectiveRow.length - 1; i++) {
            if (objectiveRow[i].compareTo(Value.ZERO) < 0) {
                return true;
            }
        }
        return false;
    }

    private int findPivotColumn() {
        Value[] objectiveRow = tableau[0];
        for (int i = 0; i < objectiveRow.length; i++) {
            if (objectiveRow[i].compareTo(Value.ZERO) < 0) {
                return i;
            }
        }
        return -1;
    }

    private int findPivotRow(int pivotColumn) {
        int pivotRow = -1;
        Value minRatio = Value.POSITIVE_INFINITY;
        for (int i = 1; i < tableau.length; i++) {
            Value[] row = tableau[i];
            if (row[pivotColumn].compareTo(Value.ZERO) > 0) {
                Value ratio = row[row.length - 1].divide(row[pivotColumn]);
                if (ratio.compareTo(minRatio) < 0) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        return pivotRow;
    }

    private void pivot(int pivotRow, int pivotColumn) {
        Value[] pivotRowValues = tableau[pivotRow];
        Value pivotValue = pivotRowValues[pivotColumn];

        // Normalizza la riga pivot
        for (int i = 0; i < pivotRowValues.length; i++) {
            pivotRowValues[i] = pivotRowValues[i].divide(pivotValue);
        }

        // Aggiorna le altre righe
        for (int i = 0; i < tableau.length; i++) {
            if (i != pivotRow) {
                Value[] row = tableau[i];
                Value multiplier = row[pivotColumn];
                for (int j = 0; j < row.length; j++) {
                    Value newValue = row[j].subtract(multiplier.multiply(pivotRowValues[j]));
                    row[j] = newValue;
                }
            }
        }
    }

    public void printTableau() {
        printTableau(tableau);
    }

    public static void printTableau(Value[][] tableau) {
        for (Value[] row : tableau) {
            for (Value value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Value getObjectiveValue() {
        return tableau[0][0];
    }

    public static boolean isBasis(int column, Value[][] tableau) {
        boolean basicVariable = false;
        int basicRow = -1;
        for (int row = 1; row < tableau.length; row++) {
            if (tableau[row][column].equals(Value.ONE)) {
                if (basicRow == -1) {
                    basicRow = row;
                    basicVariable = true;
                } else {
                    basicVariable = false;
                    break;
                }
            } else if (!tableau[row][column].equals(Value.ZERO)) {
                basicVariable = false;
                break;
            }
        }
        return basicVariable;
    }

    public static List<Value[]> getBasis(Value[][] tableau, int numVariables, int numConstraints) {
        List<Value[]> basis = new ArrayList<>();
        for (int column = 0; column < numVariables; column++) {
            boolean basicVariable = true;
            int basicRow = -1;
            for (int row = 1; row < tableau.length; row++) {
                if (tableau[row][column].equals(Value.ONE)) {
                    if (basicRow == -1) {
                        basicRow = row;
                    } else {
                        basicVariable = false;
                        break;
                    }
                } else if (!tableau[row][column].equals(Value.ZERO)) {
                    basicVariable = false;
                    break;
                }
            }
            if (basicVariable && basicRow != -1) {
                // Ricostruisco la colonna
                Value[] basisColumn = new Value[numConstraints];
                for (int i = 0; i < numConstraints; i++) {
                    basisColumn[i] = i == basicRow - 1 ? Value.ONE : Value.ZERO;
                }
                basis.add(basisColumn);
            }
        }
        return basis;
    }

    public Pair<Integer, Integer> getCostoRidottoDaAggiornare() {
        for (int column = 0; column < numVariables; column++) {
            boolean basicVariable = true;
            int basicRow = -1;
            for (int row = 1; row < tableau.length; row++) {
                if (tableau[row][column].equals(Value.ONE)) {
                    if (basicRow == -1) {
                        basicRow = row;
                    } else {
                        basicVariable = false;
                        break;
                    }
                } else if (!tableau[row][column].equals(Value.ZERO)) {
                    basicVariable = false;
                    break;
                }
            }
            if (basicVariable && !tableau[0][column].equals(Value.ZERO)) {
                // Controlla se il costo ridotto è 0
                return Pair.of(basicRow, column);
            }
        }
        return Pair.of(-1, -1);
    }

    public List<Message> getPassaggi() {
        return passaggi;
    }

    public Value[][] getTableau() {
        return tableau;
    }

}
