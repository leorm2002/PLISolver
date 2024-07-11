package it.naddeil.ro.gomorysolver;

import java.util.Arrays;



import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.simplexsolver.SimplexSolver;



public class GomorySolver  {
    private final SimplexSolver simplexSolver;
    private final DualSimplexSolver dualSimplexSolver;
    // Vincoli:
    // Tutti i segni della funzione obbiettivo positivi
    public GomorySolver(SimplexSolver simplexSolver, DualSimplexSolver dualSimplexSolver) {
        this.simplexSolver = simplexSolver;
        this.dualSimplexSolver = dualSimplexSolver;
    }

    boolean isSolved(FracResult r) {
        Fraction[] s = r.getSoluzione();
        for (int i= 0; i < s.length; i++){
            if(!s[i].isInteger()){
                return false;
            }
        }
        return true;
    }

    static Fraction f(Fraction x) {
        return x.subtract(x.floor());
    }

    public static Fraction[] creaTaglio(Fraction[] riga) {
        // Un taglio è del tipo f(c[1])x_1 + f(c[2])x_2 + ... + f(c[n])x_n >= f(c[0])
        // Dove f(x) è definita come x -> x - floor(x)
        Fraction[] taglio = new Fraction[riga.length];
        for (int i = 0; i < riga.length ; i++) {
            taglio[i] =  f(riga[i]);
        }
        return taglio;
    }

    public static int trovaRigaConValoreFrazionario(Fraction[] matrice) {
        int riga = -1;
        for (int i = 0; i < matrice.length; i++) {
            if (!matrice[i].isInteger()) {
                riga = i + 1;
                break;
            }
        }
        return riga;
    }

    Fraction[][] aggiungiVincolo(Fraction[][] tableau, Fraction[] taglio, Fraction[] b, Fraction z) {
        int nRighe = tableau.length;
        int nColonne = tableau[0].length;
        Fraction[][] nuovaMatrice = new Fraction[nRighe + 1][nColonne + 1];

        // Ricopio la matrice settando 0 alla fine di ogni riga
        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne - 1; j++) {
                nuovaMatrice[i][j] = tableau[i][j];
            }
            nuovaMatrice[i][nColonne - 1] = Fraction.ZERO;  // Aggiungi 0 alla fine
        }

        // Aggiungo il taglio alla fine
        for (int i = 0; i < nColonne -1; i++) {
            nuovaMatrice[nRighe][i] = taglio[i];
        }
        // Variabile di slack per il taglio
        nuovaMatrice[nRighe][nColonne - 1] = Fraction.ONE;
        // Riporto il valore di b per il taglio
        nuovaMatrice[nRighe][nColonne] = taglio[taglio.length - 1];
        // Riporto soluzione
        nuovaMatrice[0][nColonne] =z;

        // Riporto il vettore b
        for (int i = 1; i < nRighe; i++) {
            nuovaMatrice[i][nColonne] = b[i - 1];
        }
        return nuovaMatrice;
    }

    Fraction[] creaTaglio(Fraction[] b, Fraction[][] tableau) {
        // 0. Estraggo l'ultima colonna ovvero i valori di b
        // 1. trova riga su cui aggiungere taglio (riga con coefficiente frazionario più grande)
        int rigaTaglio = trovaRigaConValoreFrazionario(b);
        Fraction[] riga = tableau[rigaTaglio];

        // 2. calcola taglio
        return  Arrays.stream(creaTaglio(riga)).map(Fraction::negate).toArray(Fraction[]::new);
        
    }
    
    public void printTableau(Fraction[][] tableau) {
        for (Fraction[] row : tableau) {
            for (Fraction val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printTableauD(Fraction[][] tableau) {
        for (Fraction[] row : tableau) {
            for (Fraction val : row) {
                System.out.print(val.doubleValue() + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public FracResult solve(PublicProblem problem, Parameters parameters) {
        int maxIter = 300;
        long startTime = System.nanoTime();

        FracResult rs = simplexSolver.solve(problem);
        printTableau(rs.getTableau());
        System.out.println("Z: " + rs.getZ());
        printTableauD(rs.getTableau());
        long endTime = System.nanoTime();
        System.out.println("Tempo: " + (endTime - startTime) / 1000000);

        int i = 0;
        while (i < maxIter) {

        if (!isSolved(rs)) {
                // Calcolo il taglio
                Fraction[] taglio = creaTaglio(rs.getSoluzione(), rs.getTableau());
                Fraction[][] newA = aggiungiVincolo(rs.getTableau(), taglio, rs.getSoluzione(), rs.getZ());

                rs = dualSimplexSolver.riottimizza(newA);

                if(isSolved(rs)){
                    return rs;
                }
                i++;
                if(i > maxIter){
                    throw new RuntimeException("Numero massimo di iterazioni raggiunto");
                }
        }
    }

        // Todo return solution
        return null;
    }
}
