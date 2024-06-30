package it.naddeil.ro.gomorysolver;

import it.naddeil.ro.common.*;



public class GomorySolver implements PLISolver {
    private final PLSolver plSolver;

    // Vincoli:
    // Tutti i segni della funzione obbiettivo positivi
    public GomorySolver(PLSolver plsolver) {
        this.plSolver = plsolver;

    }

    boolean isSolved(Problema p, Result r) {
        return false;
    }

    double f(double x) {
        return x - Math.floor(x);
    }

    double[] creaTaglio(double[] coefficienti) {
        // Un taglio è del tipo f(c[1])x_1 + f(c[2])x_2 + ... + f(c[n])x_n >= f(c[0])
        // Dove f(x) è definita come x -> x - floor(x)
        double[] taglio = new double[coefficienti.length];
        for (int i = 0; i < coefficienti.length; i++) {
            taglio[i] = f(coefficienti[i]);
        }

        return taglio;
    }

    int trovaRigaConValoreFrazionario(double[][] matrice) {
        int riga = -1;
        for (int i = 0; i < matrice.length; i++) {
            double[] rigaMatrice = matrice[i];
            double valorePrimoElemento = rigaMatrice[0];
            if (valorePrimoElemento % 1 != 0) {
                riga = i;
                break;
            }
        }
        return riga;
    }

    double[][] costruisciSistema(double[][] problemaOriginale, double[] taglio) {
        return null;
    }

    Problema aggiungiTaglio(Problema p) {
        // 1. trova riga su cui aggiungere taglio (riga con coefficiente frazionario più grande)
        //int rigaTaglio = trovaRigaConValoreFrazionario(p.getA());
        // 2. calcola taglio
        //double[] taglio = creaTaglio(p.getA()[rigaTaglio]);
        // 3. esprimi calcolo rispetto a base
        //double[][] sistema = costruisciSistema(p.getA(), taglio);
        double[] vincolo = null;// GaussianSolver.gaussianElimination(sistema, 10);
        // 4. aggiungi vincolo
        //p.addConstraint(vincolo);

        return p;

    }

    @Override
    public Result solve(Problema problem, Parameters parameters) {
        int maxIter = 10;
        for (int i = 0; i < maxIter; i++) {
            final Result r = plSolver.solve(problem, parameters);
            if (isSolved(problem, r)) {
                break;
            }
            problem = aggiungiTaglio(problem);
        }

        // Todo return solution
        return null;
    }

}
