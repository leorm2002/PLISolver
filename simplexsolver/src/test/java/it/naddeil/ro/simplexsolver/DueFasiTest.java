package it.naddeil.ro.simplexsolver;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import it.naddeil.ro.common.Fraction;

public class DueFasiTest {

    @Test
    void testGetIdentity(){
        Fraction[][] identity = DueFasi.getIdentity(3);
        Fraction[][] expected = new Fraction[][]{
            {Fraction.ONE,Fraction.ZERO,Fraction.ZERO},
            {Fraction.ZERO,Fraction.ONE,Fraction.ZERO},
            {Fraction.ZERO,Fraction.ZERO,Fraction.ONE}
        };
        for (int i = 0; i < identity.length; i++) {
            for (int j = 0; j < identity[i].length; j++) {
                assert(identity[i][j].equals(expected[i][j]));
            }
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
    void testAggiuntaVariabiliSintetiche(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO},
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO},
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO}
        };
        Fraction[][] nuovoTableau = DueFasi.aggiungiVariabiliSintetiche(tableau).tableau();
        Fraction[][] tableauExp = new Fraction[][]{
            {Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.ONE,Fraction.ZERO},
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO,Fraction.ZERO},
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO,Fraction.ONE,Fraction.ZERO}
        };

        assertEqualsT(tableauExp, nuovoTableau);
    }
    @Test
    void testAggiuntaVariabiliSintetiche2(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO},
            {Fraction.ZERO,Fraction.ONE,Fraction.ONE,Fraction.ZERO},
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO}
        };
        Fraction[][] nuovoTableau = DueFasi.aggiungiVariabiliSintetiche(tableau).tableau();
        Fraction[][] tableauExp = new Fraction[][]{
            {Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ONE,Fraction.ZERO},
            {Fraction.ZERO,Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO},
            {Fraction.ONE,Fraction.ONE,Fraction.ONE,Fraction.ZERO,Fraction.ZERO}
        };

        assertEqualsT(tableauExp, nuovoTableau);
    }

    @Test
    void testApplicaMetodoDueFasi(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.ONE,Fraction.ZERO,Fraction.ZERO,Fraction.ZERO,Fraction.ZERO},
            {Fraction.ZERO,Fraction.ONE,new Fraction(2),new Fraction(3),new Fraction(15)},
            {Fraction.ZERO,new Fraction(2),new Fraction(1),new Fraction(5),new Fraction(20)},
            {Fraction.ONE,Fraction.ONE,new Fraction(2),new Fraction(1),new Fraction(10)},
        };

        var out = DueFasi.applicaMetodoDueFasi(tableau);
        Fraction[][] expected = new Fraction[][] {
            {Fraction.ONE, Fraction.ZERO, Fraction.ZERO, Fraction.ZERO, Fraction.ZERO},
            {Fraction.of(-1,2), Fraction.ZERO, Fraction.ZERO, Fraction.ONE, Fraction.of(5, 2)},
            {Fraction.of(7,6), Fraction.ONE, Fraction.ZERO,Fraction.ZERO, Fraction.of(5, 2)},
            {Fraction.of(1,6), Fraction.ZERO, Fraction.ONE, Fraction.ZERO, Fraction.of(5,2)}
        };

        assertEqualsT(expected, out);
    }
    @Test
    void testApplicaMetodoDueFasi2(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.ONE,Fraction.ZERO,Fraction.ONE,Fraction.ZERO,Fraction.ZERO},

            {Fraction.ONE, new Fraction(2),Fraction.ZERO,Fraction.ONE,new Fraction(5)},
            {Fraction.ZERO,Fraction.ONE,Fraction.of(2),Fraction.ZERO,new Fraction(6)},

        };

        var out = DueFasi.applicaMetodoDueFasi(tableau);

        Fraction[][] tableauExp = new Fraction[][]{
            {Fraction.of(5,4),Fraction.ZERO,Fraction.ZERO,Fraction.of(1,4),Fraction.of(-7,4)},
            {Fraction.of(1,2), Fraction.ONE, Fraction.ZERO, Fraction.of(1,2),Fraction.of(5,2)},
            {Fraction.of(-1,4),Fraction.ZERO,Fraction.ONE,Fraction.of(-1,4),Fraction.of(7,4)}
        };

        assertEqualsT(tableauExp, out);
    }


    @Test
    void testApplicaMetodoDueFasiDegenere(){
        // Fase 1 degenere
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-3),Fraction.of(-2),Fraction.of(-1),Fraction.ZERO,Fraction.ZERO, Fraction.ZERO},

            {Fraction.of(2), Fraction.ONE, Fraction.ONE, Fraction.ONE, Fraction.ZERO, Fraction.of(2)},
            {Fraction.of(3), Fraction.of(4), Fraction.of(2), Fraction.ZERO, Fraction.MINUS_ONE, Fraction.of(8)}
        };

        var out = DueFasi.applicaMetodoDueFasi(tableau);

        Fraction[][] tableauExp = new Fraction[][]{
            {Fraction.ONE,Fraction.ZERO,Fraction.ONE,Fraction.of(2),Fraction.ZERO,Fraction.of(4)},
            {Fraction.of(2), Fraction.ONE, Fraction.ONE, Fraction.ONE, Fraction.ZERO, Fraction.of(2)},
            {Fraction.of(5), Fraction.ZERO, Fraction.of(2), Fraction.of(4), Fraction.of(1)}
        };

        assertEquals(Fraction.of(4), out[0][out[0].length - 1]);
        assertEqualsT(tableauExp,out);
    }

    @Test
    void dueFasiVincoloRidondante(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-5), Fraction.of(-4), Fraction.of(-1), Fraction.of(-1), Fraction.ZERO},
            {Fraction.ONE, Fraction.of(2), Fraction.ONE, Fraction.ZERO, Fraction.of(10)},
            {Fraction.of(5), Fraction.of(5), Fraction.of(2), Fraction.ONE, Fraction.of(30)},
            {Fraction.of(3), Fraction.ONE, Fraction.ZERO, Fraction.ONE, Fraction.of(10)}
        };
        var out = DueFasi.applicaMetodoDueFasi(tableau);

        Fraction[][] tableauExpected = new Fraction[][]{
            {Fraction.ZERO, Fraction.ZERO, Fraction.of(2,5), Fraction.of(1,5), Fraction.of(26)},
            {Fraction.ZERO, Fraction.ONE, Fraction.of(3,5), Fraction.of(-1,5), Fraction.of(4)},
            {Fraction.ONE, Fraction.ZERO, Fraction.of(-1,5), Fraction.of(2,5), Fraction.of(2)}
        };

        assertEqualsT(tableauExpected, out);
    }

    @Test
    void testProblemaIllimitato(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-4), Fraction.MINUS_ONE, Fraction.ZERO, Fraction.ZERO, Fraction.ZERO},
            {Fraction.MINUS_ONE, Fraction.ONE, Fraction.ONE, Fraction.ZERO},
            {Fraction.of(3), Fraction.of(-4), Fraction.ZERO, Fraction.ONE, Fraction.ZERO}
        };
        try {
            var out = DueFasi.applicaMetodoDueFasi(tableau);
            
        } catch (RuntimeException e) {
            // TODO: handle exception
        }catch (Exception e) {
            fail();
        }
    }

}
