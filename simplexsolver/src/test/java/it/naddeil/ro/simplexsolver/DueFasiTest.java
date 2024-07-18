package it.naddeil.ro.simplexsolver;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;

public class DueFasiTest {

    @Test
    void testGetIdentity(){
        Comp[][] identity = DueFasi.getIdentity(3);
        Comp[][] expected = new Comp[][]{
            {Comp.ONE,Comp.ZERO,Comp.ZERO},
            {Comp.ZERO,Comp.ONE,Comp.ZERO},
            {Comp.ZERO,Comp.ZERO,Comp.ONE}
        };
        for (int i = 0; i < identity.length; i++) {
            for (int j = 0; j < identity[i].length; j++) {
                assert(identity[i][j].equals(expected[i][j]));
            }
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
    void testAggiuntaVariabiliSintetiche(){
        Fraction[][] tableau = new Fraction[][]{
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO},
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO},
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO}
        };
        Comp[][] nuovoTableau = DueFasi.aggiungiVariabiliSintetiche(tableau).tableau();
        Comp[][] tableauExp = new Comp[][]{
            {Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ONE,Comp.ONE,Comp.ZERO},
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO,Comp.ZERO},
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO,Comp.ONE,Comp.ZERO}
        };

        assertEqualsT(tableauExp, nuovoTableau);
    }
    @Test
    void testAggiuntaVariabiliSintetiche2(){
        Fraction[][] tableau = new Fraction[][]{
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO},
            {Comp.ZERO,Comp.ONE,Comp.ONE,Comp.ZERO},
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO}
        };
        Comp[][] nuovoTableau = DueFasi.aggiungiVariabiliSintetiche(tableau).tableau();
        Comp[][] tableauExp = new Comp[][]{
            {Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ONE,Comp.ZERO},
            {Comp.ZERO,Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO},
            {Comp.ONE,Comp.ONE,Comp.ONE,Comp.ZERO,Comp.ZERO}
        };

        assertEqualsT(tableauExp, nuovoTableau);
    }

    @Test
    void testApplicaMetodoDueFasi(){
        Comp[][] tableau = new Comp[][]{
            {Comp.ONE,Comp.ZERO,Comp.ZERO,Comp.ZERO,Comp.ZERO},
            {Comp.ZERO,Comp.ONE,new Fraction(2),new Fraction(3),new Fraction(15)},
            {Comp.ZERO,new Fraction(2),new Fraction(1),new Fraction(5),new Fraction(20)},
            {Comp.ONE,Comp.ONE,new Fraction(2),new Fraction(1),new Fraction(10)},
        };

        Comp[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());
        Comp[][] expected = new Comp[][] {
            {Comp.ONE, Comp.ZERO, Comp.ZERO, Comp.ZERO, Comp.ZERO},
            {Fraction.of(-1,2), Comp.ZERO, Comp.ZERO, Comp.ONE, Fraction.of(5, 2)},
            {Fraction.of(7,6), Comp.ONE, Comp.ZERO,Comp.ZERO, Fraction.of(5, 2)},
            {Fraction.of(1,6), Comp.ZERO, Comp.ONE, Comp.ZERO, Fraction.of(5,2)}
        };

        assertEqualsT(expected, out);
    }
    @Test
    void testApplicaMetodoDueFasi2(){
        Fraction[][] tableau = new Fraction[][]{
            {Comp.ONE,Comp.ZERO,Comp.ONE,Comp.ZERO,Comp.ZERO},

            {Comp.ONE, new Fraction(2),Comp.ZERO,Comp.ONE,new Fraction(5)},
            {Comp.ZERO,Comp.ONE,Fraction.of(2),Comp.ZERO,new Fraction(6)},

        };

        Comp[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());

        Comp[][] tableauExp = new Comp[][]{
            {Fraction.of(5,4),Comp.ZERO,Comp.ZERO,Fraction.of(1,4),Fraction.of(-7,4)},
            {Fraction.of(1,2), Comp.ONE, Comp.ZERO, Fraction.of(1,2),Fraction.of(5,2)},
            {Fraction.of(-1,4),Comp.ZERO,Comp.ONE,Fraction.of(-1,4),Fraction.of(7,4)}
        };

        assertEqualsT(tableauExp, out);
    }


    @Test
    void testApplicaMetodoDueFasiDegenere(){
        // Fase 1 degenere
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-3),Fraction.of(-2),Fraction.of(-1),Comp.ZERO,Comp.ZERO, Comp.ZERO},

            {Fraction.of(2), Comp.ONE, Comp.ONE, Comp.ONE, Comp.ZERO, Fraction.of(2)},
            {Fraction.of(3), Fraction.of(4), Fraction.of(2), Comp.ZERO, Comp.MINUS_ONE, Fraction.of(8)}
        };

        Comp[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());

        Comp[][] tableauExp = new Comp[][]{
            {Comp.ONE,Comp.ZERO,Comp.ONE,Fraction.of(2),Comp.ZERO,Fraction.of(4)},
            {Fraction.of(2), Comp.ONE, Comp.ONE, Comp.ONE, Comp.ZERO, Fraction.of(2)},
            {Fraction.of(5), Comp.ZERO, Fraction.of(2), Fraction.of(4), Fraction.of(1)}
        };

        assertEquals(Fraction.of(4), out[0][out[0].length - 1]);
        assertEqualsT(tableauExp,out);
    }

    @Test
    void dueFasiVincoloRidondante(){
        Comp[][] tableau = new Fraction[][]{
            {Fraction.of(-5), Fraction.of(-4), Fraction.of(-1), Fraction.of(-1), Comp.ZERO},
            {Comp.ONE, Fraction.of(2), Comp.ONE, Comp.ZERO, Fraction.of(10)},
            {Fraction.of(5), Fraction.of(5), Fraction.of(2), Comp.ONE, Fraction.of(30)},
            {Fraction.of(3), Comp.ONE, Comp.ZERO, Comp.ONE, Fraction.of(10)}
        };
        Comp[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());

        Comp[][] tableauExpected = new Comp[][]{
            {Comp.ZERO, Comp.ZERO, Fraction.of(2,5), Fraction.of(1,5), Fraction.of(26)},
            {Comp.ZERO, Comp.ONE, Fraction.of(3,5), Fraction.of(-1,5), Fraction.of(4)},
            {Comp.ONE, Comp.ZERO, Fraction.of(-1,5), Fraction.of(2,5), Fraction.of(2)}
        };

        assertEqualsT(tableauExpected, out);
    }

    @Test
    void testProblemaIllimitato(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-4), Comp.MINUS_ONE, Comp.ZERO, Comp.ZERO, Comp.ZERO},
            {Comp.MINUS_ONE, Comp.ONE, Comp.ONE, Comp.ZERO},
            {Fraction.of(3), Fraction.of(-4), Comp.ZERO, Comp.ONE, Comp.ZERO}
        };
        try {
            Comp[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());
            
        } catch (RuntimeException e) {
            // TODO: handle exception
        }catch (Exception e) {
            fail();
        }
    }

}
