package it.naddeil.ro.simplexsolver;


import static org.junit.Assert.assertEquals;

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
            {Fraction.ONE,Fraction.MINUS_ONE,new Fraction(-2),new Fraction(-3),Fraction.ZERO},
            {Fraction.ZERO,Fraction.ONE,new Fraction(2),new Fraction(3),new Fraction(15)},
            {Fraction.ZERO,new Fraction(2),new Fraction(2),new Fraction(3),new Fraction(20)},
            {Fraction.ONE,Fraction.ONE,new Fraction(2),new Fraction(1),new Fraction(10)},
        };

        var out = DueFasi.applicaMetodoDueFasi(tableau);
        //  5 * x2 + 5/4x3 x1 + 5/2 x4 = 0
        // 0 - 10 - 5/2 - 3* 5/2 
        //  -10 - 4 * 5/2
        //  -10 - 10
        //  -20
        // PDPD
        int x = 10;
    }
    @Test
    void testApplicaMetodoDueFasi2(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.ONE,Fraction.ZERO,Fraction.ONE,Fraction.ZERO,Fraction.ZERO},

            {Fraction.ONE, new Fraction(2),Fraction.ZERO,Fraction.ONE,new Fraction(5)},
            {Fraction.ZERO,Fraction.ONE,new Fraction(2),Fraction.ZERO,new Fraction(6)},

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
    void testApplicaMetodoDueFasi3(){
        // Fase 1 degenere
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-3),Fraction.of(-2),Fraction.of(-1),Fraction.ZERO,Fraction.ZERO, Fraction.ZERO},

            {Fraction.of(2), Fraction.ONE, Fraction.ONE, Fraction.ONE, Fraction.ZERO, Fraction.of(2)},
            {Fraction.of(3), Fraction.of(4), Fraction.of(2), Fraction.ZERO, Fraction.MINUS_ONE, Fraction.of(8)}
        };

        var out = DueFasi.applicaMetodoDueFasi(tableau);

        Fraction[][] tableauExp = new Fraction[][]{
            {Fraction.of(5,4),Fraction.ZERO,Fraction.ZERO,Fraction.of(1,4),Fraction.of(-7,4)},
            {Fraction.of(1,2), Fraction.ONE, Fraction.ZERO, Fraction.of(1,2),Fraction.of(5,2)},
            {Fraction.of(-1,4),Fraction.ZERO,Fraction.ONE,Fraction.of(-1,4),Fraction.of(7,4)}
        };

        assertEquals(Fraction.of(4), out[0][out[0].length - 1]);
    }

}
