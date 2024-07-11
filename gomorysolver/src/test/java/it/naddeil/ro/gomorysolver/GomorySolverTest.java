package it.naddeil.ro.gomorysolver;

import static org.junit.Assert.assertEquals;

import org.ejml.interfaces.linsol.LinearSolver;
import org.junit.jupiter.api.Test;
import java.util.List;

import it.naddeil.ro.common.FracResult;
import it.naddeil.ro.common.Fraction;
import it.naddeil.ro.common.FunzioneObbiettivo;
import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.Tipo;
import it.naddeil.ro.common.Verso;
import it.naddeil.ro.common.Vincolo;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.gomorysolver.GomorySolver;
import it.naddeil.ro.simplexsolver.SimplexSolver;

class GomorySolverTest {
    @Test
    void test1(){
        SimplexSolver ss = new SimplexSolver(){
            @Override
                public FracResult solve(PublicProblem problem) {
                    Fraction[][] tableauu = {
                        {new Fraction(0), new Fraction(0), new Fraction(-1,4), new Fraction(-1,4), new Fraction(-3,2)},
                        {Fraction.ONE, Fraction.ZERO, new Fraction(1,6), new Fraction(-1,6),Fraction.ONE},
                        {Fraction.ZERO, Fraction.ONE, new Fraction(1,4), new Fraction(1,4), new Fraction(3,2)}
                    };
                    return FracResult.fromTableau(tableauu);
                }
            };

            DualSimplexSolver ds = new DualSimplexSolver();
            GomorySolver gs = new GomorySolver(ss, ds);
            FracResult result =  gs.solve(new PublicProblem(), new Parameters());
            assertEquals(new Fraction(-1), result.getZ());
        }


        @Test
        void test2(){
            SimplexSolver ss = new SimplexSolver(){
                @Override
                    public FracResult solve(PublicProblem problem) {
                        Fraction[][] tableauu = {
                            {Fraction.ZERO, new Fraction(-7),Fraction.ZERO, new Fraction(-8,10), Fraction.ZERO, new Fraction(-12,10), new Fraction(-4,10)},
                            {Fraction.ONE, Fraction.ZERO,Fraction.ZERO,new Fraction(2,10), Fraction.ZERO, new Fraction(-2,10),new Fraction(6,10)},
                            {Fraction.ZERO, new Fraction(-15, 10),Fraction.ZERO,new Fraction(-3,10), Fraction.ONE,new Fraction(8,10),new Fraction(16,10)},
                            {Fraction.ZERO, new Fraction(-5,10),Fraction.ONE,new Fraction(-1,10), Fraction.ZERO, new Fraction(-4,10),new Fraction(2,10)},
                        };
                        return FracResult.fromTableau(tableauu);
                    }
                };

                DualSimplexSolver ds = new DualSimplexSolver();
                GomorySolver gs = new GomorySolver(ss, ds);
                FracResult result =  gs.solve(new PublicProblem(), new Parameters());
                assertEquals(new Fraction(2), result.getZ());
            }

    @Test
    void testTrovaRigaConValoreFrazionario() {
        GomorySolver gomorySolver = new GomorySolver(null, null);

        Fraction[] matrice1 = { new Fraction(1), new Fraction(2), new Fraction(3) };
        int result1 = gomorySolver.trovaRigaConValoreFrazionario(matrice1);
        assertEquals(-1, result1);

        Fraction[] matrice2 = { new Fraction(1), new Fraction(5,2), new Fraction(3) };
        int result2 = gomorySolver.trovaRigaConValoreFrazionario(matrice2);
        assertEquals(2, result2);

        Fraction[] matrice3 = { new Fraction(1), new Fraction(2), new Fraction(3,7) };
        int result3 = gomorySolver.trovaRigaConValoreFrazionario(matrice3);
        assertEquals(3, result3);
    }

    void assertArrayEquals(Fraction[] expected, Fraction[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
    void assertEqualsT(Fraction[][] expected, Fraction[][] actual){
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assert(expected[i][j].equals(actual[i][j]));
            }
        }
    }
    @Test
    void testCreaTaglio() {
        GomorySolver gomorySolver = new GomorySolver(null, null);

        Fraction[] riga1 = { new Fraction(1), new Fraction(2), new Fraction(3) };
        Fraction[] expected1 = { new Fraction(0), new Fraction(0), new Fraction(0) };
        assertArrayEquals(expected1, gomorySolver.creaTaglio(riga1));
        
        Fraction[] riga2 = { new Fraction(1), new Fraction(5, 2), new Fraction(3) };
        Fraction[] expected2 = { new Fraction(0), new Fraction(1,2), new Fraction(0) };
        assertArrayEquals(expected2, gomorySolver.creaTaglio(riga2));

        Fraction[] riga3 = { new Fraction(1), new Fraction(2), new Fraction(3, 7) };
        Fraction[] expected3 = { new Fraction(0), new Fraction(0), new Fraction(3, 7) };
        assertArrayEquals(expected3, gomorySolver.creaTaglio(riga3));
    }

    @Test
    void testGomoryCompleto(){
        GomorySolver solver = new GomorySolver(new SimplexSolver(),  new DualSimplexSolver());

        PublicProblem p = new PublicProblem();
        FunzioneObbiettivo f = new FunzioneObbiettivo();
        f.setTipo(Tipo.MAX);
        f.setC(List.of(0d,1d));

        p.setVincoli(List.of(
            new Vincolo(List.of(3d,2d,6d), Verso.LE),
            new Vincolo(List.of(-3d,2d,0d), Verso.LE)
        ));

        p.setFunzioneObbiettivo(f);

        FracResult out = solver.solve(p, new Parameters());
        Fraction[][] tableau = out.getTableau();

        Fraction[][] expected = new Fraction[][]{
            {Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.ZERO,Fraction.ONE},
            {Fraction.ONE,Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.of(-1,2),Fraction.ONE},
            {Fraction.ZERO,Fraction.ONE,Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.ZERO,Fraction.ONE},
            {Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.ZERO,Fraction.of(-5),Fraction.of(3,2),Fraction.ONE},
            {Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.ONE,Fraction.of(-3,2),Fraction.ONE}
        };

        

        

    }
}