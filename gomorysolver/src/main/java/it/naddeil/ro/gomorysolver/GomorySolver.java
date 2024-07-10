package it.naddeil.ro.gomorysolver;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;


import it.naddeil.ro.common.*;
import it.naddeil.ro.common.pub.PublicProblem;



public class GomorySolver implements PLISolver {
    private final PLSolver plSolver;

    // Vincoli:
    // Tutti i segni della funzione obbiettivo positivi
    public GomorySolver(PLSolver plsolver) {
        this.plSolver = plsolver;

    }


    boolean isInteger(double x) {
        double tollleranza = new Parameters().getTollleranza();
        return ConfrontationUtils.areEqual(x % 1, 0, tollleranza) || ConfrontationUtils.areEqual(x % 1, 1, tollleranza);
    }

    boolean isSolved(Result r) {
        SimpleMatrix s = r.getSoluzione();
        for (int i= 0; i < s.numRows(); i++){
            Double valore = s.get(i);
            if(!isInteger(valore)){
                return false;
            }
        }
        return true;
    }

    double f(double x) {
        return x - Math.floor(x);
    }

    SimpleMatrix creaTaglio(SimpleMatrix riga, SimpleMatrix b) {
        // Un taglio è del tipo f(c[1])x_1 + f(c[2])x_2 + ... + f(c[n])x_n >= f(c[0])
        // Dove f(x) è definita come x -> x - floor(x)
        SimpleMatrix taglio = new SimpleMatrix(1, riga.numCols() );
        for (int i = 0; i < riga.numCols() ; i++) {
            taglio.set(i, f(riga.get(i)));
        }

        return taglio;
    }

    int trovaRigaConValoreFrazionario(SimpleMatrix matrice) {
        int riga = -1;
        for (int i = 0; i < matrice.numRows(); i++) {
            if (!isInteger(matrice.get(i))) {
                riga = i;
                break;
            }
        }
        return riga;
    }

    SimpleMatrix aggiungiVincolo(SimpleMatrix matrice, SimpleMatrix taglio) {
        int nRighe = matrice.numRows();
        int nColonne = matrice.numCols();
        SimpleMatrix nuovaMatrice = new SimpleMatrix(nRighe + 1, nColonne + 1);

        // Ricopio la matrice settando 0 alla fine di ogni riga
        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne; j++) {
                nuovaMatrice.set(i, j, matrice.get(i, j));
            }
            nuovaMatrice.set(i, nColonne,0d);  // Aggiungi 0 alla fine
        }

        // Aggiungo il taglio alla fine
        for (int j = 0; j < nColonne; j++) {
            nuovaMatrice.set(nRighe, j, taglio.get(j));
        }
        // Variabile di slack per il taglio
        nuovaMatrice.set(nRighe, nColonne, 1d);

        return nuovaMatrice;
    }

    SimpleMatrix creaTaglio(SimpleMatrix tableau) {
        // 0. Estraggo l'ultima colonna ovvero i valori di b
        SimpleMatrix b = tableau.extractVector(false, tableau.numCols()-1);
        // 1. trova riga su cui aggiungere taglio (riga con coefficiente frazionario più grande)
        int rigaTaglio = trovaRigaConValoreFrazionario(b);
        SimpleMatrix riga = tableau.extractVector(true, rigaTaglio);

        // 2. calcola taglio
        return creaTaglio(riga, b);
    }

    SimpleMatrix createNewC(SimpleMatrix tableau){
        SimpleMatrix c = tableau.extractVector(true, tableau.numRows()-1);
        SimpleMatrix newC = new SimpleMatrix(1, c.numCols());
        for (int i = 0; i < c.numCols(); i++) {
            newC.set(i, f(c.get(i)));
        }
        newC.set(c.numCols() - 1,0);
        return newC;
    }

    SimpleMatrix createNewB(SimpleMatrix tableau, SimpleMatrix taglio){
        SimpleMatrix b = tableau.extractVector(false, tableau.numCols()-1);
        SimpleMatrix newB = new SimpleMatrix(b.numRows(), 1);
        for (int i = 1; i < b.numRows(); i++) {
            newB.set(i -1, b.get(i));
        }
        newB.set(b.numRows() -1,taglio.get(taglio.numCols()-1));
        return newB;
    }

    @Override
    public Result solve(PublicProblem problem, Parameters parameters) {
        int maxIter = 300;
        long startTime = System.nanoTime();

        Result r = plSolver.solve(problem, parameters);
        long endTime = System.nanoTime();
        System.out.println("Tempo: " + (endTime - startTime) / 1000000);

        int i = 0;
        while (i < maxIter) {
            
        if (!isSolved(r)) {
                // Estraggo la matrice A dal tableau rimuovendo la prima riga e l'ultima colonna
                SimpleMatrix tableau = r.getTableauOttimo();
                SimpleMatrix a = tableau.extractMatrix(1, tableau.numRows(), 0, tableau.numCols()-1);

                // Calcolo il taglio
                SimpleMatrix taglio = creaTaglio(tableau).negative();
                SimpleMatrix newA = aggiungiVincolo(a, taglio);

                // Definisco le basi del nuovo problema come le vecchie pià la variabile di slack del taglio
                List<Integer> basis = new ArrayList<>(r.getBasis());
                basis.add(newA.numCols()-1);

                r = plSolver.reOptimize(new Problema(newA,createNewB(tableau, taglio),createNewC(tableau).negative(), basis ), parameters);
                System.out.println(r.getSoluzione());
                System.out.println(i);
                if(isSolved(r)){
                    return r;
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
/*
 * 
    public static void main(String[] args) {
        PublicProblem p = new PublicProblem();
        FunzioneObbiettivo f = new FunzioneObbiettivo();
        f.setTipo(Tipo.MAX);
        f.setC(List.of(1d,2d));
        p.setFunzioneObbiettivo(f);
        p.setVincoli(List.of(
            new Vincolo(List.of(4d, 6d,30d), Verso.LE),
            new Vincolo(List.of(1d, -1d,1d), Verso.GE),
            new Vincolo(List.of(1d, 1d,0d), Verso.GE)
        ));


         *
          
          A = new SimpleMatrix(new double[][] {
            {1, 2, 1},
            {2, -1, 3},
        });

            c = new SimpleMatrix(new double[][] {{2, 3, 4}});
            b = new SimpleMatrix(new double[][] {{3}, {4}});


        p.setVincoli(List.of(
            new Vincolo(List.of(1d,2d, 1d,3d), Verso.GE),
            new Vincolo(List.of(2d, -1d,3d, 4d), Verso.GE)
        ));
        f.setTipo(Tipo.MIN);
        f.setC(List.of(2d, 3d, 4d));

     A = new SimpleMatrix(new double[][] {
            {1, 2, 1},
            {2, -1, 3},
        });

        c = new SimpleMatrix(new double[][] {{2, 3, 4}});
        b = new SimpleMatrix(new double[][] {{3}, {4}});

        p.setVincoli(
            List.of(
                new Vincolo(List.of(1d, 2d,  3d), Verso.GE),
                new Vincolo(List.of(2d, -1d,  4d), Verso.GE)
            )
        );

        f.setTipo(Tipo.MIN);
        f.setC(List.of(2d, 3d));

        Problema p2 = Problema.fromPublic(ProblemTransformer.portaInFormaStandard(p));
        p2.init();
        Loader.loadNativeLibraries();


        SimplessoDuale s = SimplessoDuale.createFromCanonical(p2.getA(), p2.getB(), p2.getC());

        var sol = s.solve();
        long startTime = System.nanoTime();


        GomorySolver g = new GomorySolver(new GenericSolver());
        long endTime = System.nanoTime();
        System.out.println("Tempo: " + (endTime - startTime) / 1000000);
        System.out.println(sol);

        g.solve(ProblemTransformer.portaInFormaStandard(p),null);

    }
 */

}
