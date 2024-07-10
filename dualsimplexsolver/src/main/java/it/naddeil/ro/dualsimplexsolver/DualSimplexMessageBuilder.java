package it.naddeil.ro.dualsimplexsolver;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ejml.simple.SimpleMatrix;

public class DualSimplexMessageBuilder {
    private String message ="";

    public DualSimplexMessageBuilder() {
    }

    public DualSimplexMessageBuilder nonEsisteBaseInizialeAmmissibile(){
        message += ("Non esiste una base duale ammissibile, utilizzo il metodo delle due fasi\n");
        message += ("\n");
        return this;
    }

    public DualSimplexMessageBuilder metodoDueFasi(){
        message += ("Inizio applicazione del metodo delle due fasi\n");
        message += ("\n");
        return this;
    }


    public DualSimplexMessageBuilder tableauMetodoDueFasi(SimpleMatrix a, SimpleMatrix b, SimpleMatrix c){
        message += ("Tableau del problema ausiliario con variabili artificiali\n");
        message += (formattaTableau(a, b, c));
        message += ("\n");

        return this;
    }

    public DualSimplexMessageBuilder risultatoDueFasi(SimpleMatrix tableau){
        message += ("Tableau risultato del problema ausiliario\n");
        message += (formattaTableau(tableau));
        message += ("\n");
        return this;
    }

    public DualSimplexMessageBuilder noBaseInizialeDopoDueFasi(){
        message += ("Non esiste una base ammissibile una volta effettuato il metodo delle due basi: probelma inammissibile\n");
        message += ("\n");

        return this;
    }

    public DualSimplexMessageBuilder calcolobbart(SimpleMatrix b){
        message += ("Calcolo di B^-1 * b\n");
        message += (vectorToCSV(b.transpose()));
        message += ("\n");
        message += ("\n");
        return this;
    }

    public DualSimplexMessageBuilder bnotneg(){
        message += ("B^-1 * b > 0 trovata soluzione\n");
        message += ("\n");
        return this;
    }

    public DualSimplexMessageBuilder pivot(int riga,SimpleMatrix rigaSelezionata, int colonna, int baseUscente){
        message += ("Inizio operazioni di pivotaggio\n");

        message += ("Indice riga selezionata: ");
        message += (riga);
        message += ("\n");
        message += ("Riga selezionata: ");
        message += (vectorToCSV(rigaSelezionata));
        message += ("\n");

        message += ("Indice variabile selezionata: ");
        message += (colonna);
        message += ("\n");

        message += (" base uscente: ");
        message += (baseUscente);
        message += ("\n");
        message += ("\n");
        return this;
    }

    public DualSimplexMessageBuilder calcoloc(SimpleMatrix c){
        message += ("Calcolo dei costi ridotti\n");
        message += ("\t");
        message += (vectorToCSV(c));
        message += ("\n");
        return this;
    }
    public String build() {
        return message.toString();
    }

    private static String formattaTableau(SimpleMatrix tableau){
        String  sb = "";
        int nRow = tableau.numRows();
        int nCol = tableau.numCols();
        for (int i = 0; i < nRow; i++) {
            if(i == nRow - 1){
                for (int j = 0; j < nCol; j++) {
                    sb += ("---------");
                }
                sb += ("\n");
            }
            for (int j = 0; j < nCol; j++) {
                if (j == nCol - 1) {
                    sb += (" | ");
                }
                sb += (String.format("%8.2f", tableau.get(i, j)));
            }
            
            sb += ("\n");
        }
        return sb.toString();
    
    }

    private static String formattaTableau(SimpleMatrix A, SimpleMatrix b, SimpleMatrix c) {
        String sb = "";
        int nRow = A.numRows();
        int nCol = A.numCols();
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                sb += (String.format("%8.2f", A.get(i, j)));
            }
            sb += (" | ");
            sb += (String.format("%8.2f", b.get(i)));
            sb += ("\n");
        }

        for (int j = 0; j < nCol; j++) {
            sb += ("---------");
        }
        sb += ("\n");
        for (int j = 0; j < nCol; j++) {
            sb += (String.format("%8.2f", c.get(j)));
        }
        sb += ("\n");
        return sb.toString();
    }

        public static String vectorToCSV(SimpleMatrix vector) {
        if (vector.numCols() != 1 && vector.numRows() != 1) {
            throw new IllegalArgumentException("Input must be a vector");
        }

        // Determine if it's a row vector or column vector
        boolean isRowVector = vector.numRows() == 1;

        // Stream over the elements, format them to 2 decimal places, and collect to a comma-separated string
        String csv = IntStream.range(0, isRowVector ? vector.numCols() : vector.numRows())
                .mapToObj(i -> String.format("%.2f", isRowVector ? vector.get(0, i) : vector.get(i, 0)))
                .collect(Collectors.joining(","));

        return csv;
    }
}
