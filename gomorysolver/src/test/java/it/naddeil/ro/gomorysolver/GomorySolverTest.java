package it.naddeil.ro.gomorysolver;

import static org.junit.Assert.assertEquals;

import org.ejml.interfaces.linsol.LinearSolver;
import org.junit.jupiter.api.Test;
import java.util.List;

import it.naddeil.ro.apachesimplexsolver.MySimplexSolver;
import it.naddeil.ro.common.api.FunzioneObbiettivo;
import it.naddeil.ro.common.api.Parameters;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Tipo;
import it.naddeil.ro.common.api.Verso;
import it.naddeil.ro.common.api.Vincolo;
import it.naddeil.ro.common.models.FracResult;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;
import it.naddeil.ro.dualsimplexsolver.DualSimplexSolver;
import it.naddeil.ro.simplexsolver.ConcreteSimplexSolver;

class GomorySolverTest {
    @Test
    void test1(){
        ConcreteSimplexSolver ss = new ConcreteSimplexSolver(){
            @Override
                public FracResult solve(PublicProblem problem) {
                    Fraction[][] tableauu = {
                        {new Fraction(0), new Fraction(0), new Fraction(-1,4), new Fraction(-1,4), new Fraction(-3,2)},
                        {Comp.ONE, Comp.ZERO, new Fraction(1,6), new Fraction(-1,6),Comp.ONE},
                        {Comp.ZERO, Comp.ONE, new Fraction(1,4), new Fraction(1,4), new Fraction(3,2)}
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
            ConcreteSimplexSolver ss = new ConcreteSimplexSolver(){
                @Override
                    public FracResult solve(PublicProblem problem) {
                        Fraction[][] tableauu = {
                            {Comp.ZERO, new Fraction(-7),Comp.ZERO, new Fraction(-8,10), Comp.ZERO, new Fraction(-12,10), new Fraction(-4,10)},
                            {Comp.ONE, Comp.ZERO,Comp.ZERO,new Fraction(2,10), Comp.ZERO, new Fraction(-2,10),new Fraction(6,10)},
                            {Comp.ZERO, new Fraction(-15, 10),Comp.ZERO,new Fraction(-3,10), Comp.ONE,new Fraction(8,10),new Fraction(16,10)},
                            {Comp.ZERO, new Fraction(-5,10),Comp.ONE,new Fraction(-1,10), Comp.ZERO, new Fraction(-4,10),new Fraction(2,10)},
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

        Comp[] matrice1 = { new Fraction(1), new Fraction(2), new Fraction(3) };
        int result1 = GomorySolver.trovaRigaConValoreFrazionario(matrice1);
        assertEquals(-1, result1);

        Comp[] matrice2 = { new Fraction(1), new Fraction(5,2), new Fraction(3) };
        int result2 = GomorySolver.trovaRigaConValoreFrazionario(matrice2);
        assertEquals(2, result2);

        Comp[] matrice3 = { new Fraction(1), new Fraction(2), new Fraction(3,7) };
        int result3 = GomorySolver.trovaRigaConValoreFrazionario(matrice3);
        assertEquals(3, result3);
    }

    void assertArrayEquals(Comp[] expected, Comp[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
    void assertEqualsT(Comp[][] expected, Comp[][] actual){
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assert(expected[i][j].equals(actual[i][j]));
            }
        }
    }
    @Test
    void testCreaTaglio() {
        GomorySolver gomorySolver = new GomorySolver(null, null);

        Comp[] riga1 = { new Fraction(1), new Fraction(2), new Fraction(3) };
        Comp[] expected1 = { new Fraction(0), new Fraction(0), new Fraction(0) };
        assertArrayEquals(expected1, gomorySolver.creaTaglio(riga1));

        Comp[] riga2 = { new Fraction(1), new Fraction(5, 2), new Fraction(3) };
        Comp[] expected2 = { new Fraction(0), new Fraction(1,2), new Fraction(0) };
        assertArrayEquals(expected2, gomorySolver.creaTaglio(riga2));

        Comp[] riga3 = { new Fraction(1), new Fraction(2), new Fraction(3, 7) };
        Comp[] expected3 = { new Fraction(0), new Fraction(0), new Fraction(3, 7) };
        assertArrayEquals(expected3, gomorySolver.creaTaglio(riga3));
    }

    @Test
    void testGomoryCompleto(){
        GomorySolver solver = new GomorySolver(new MySimplexSolver(),  new DualSimplexSolver());

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
        Comp[][] tableau = out.getTableau();

        Comp[][] expected = new Comp[][]{
            {Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ONE,Comp.ZERO,Comp.ONE},
            {Comp.ONE,Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ONE,Fraction.of(-1,2),Comp.ONE},
            {Comp.ZERO,Comp.ONE,Comp.ZERO,Comp.ZERO,Comp.ONE,Comp.ZERO,Comp.ONE},
            {Comp.ZERO,Comp.ZERO,Comp.ONE,Comp.ZERO,Fraction.of(-5),Fraction.of(3,2),Comp.ONE},
            {Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ONE,Comp.ONE,Fraction.of(-3,2),Comp.ONE}
        };

        

        

    }
}