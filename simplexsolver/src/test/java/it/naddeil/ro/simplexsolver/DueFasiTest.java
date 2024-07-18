package it.naddeil.ro.simplexsolver;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Value;

public class DueFasiTest {

    @Test
    void testGetIdentity(){
        Value[][] identity = DueFasi.getIdentity(3);
        Value[][] expected = new Value[][]{
            {Value.ONE,Value.ZERO,Value.ZERO},
            {Value.ZERO,Value.ONE,Value.ZERO},
            {Value.ZERO,Value.ZERO,Value.ONE}
        };
        for (int i = 0; i < identity.length; i++) {
            for (int j = 0; j < identity[i].length; j++) {
                assert(identity[i][j].equals(expected[i][j]));
            }
        }
    }

    void assertEqualsT(Value[][] expected, Value[][] actual){
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assert(expected[i][j].equals(actual[i][j]));
            }
        }
    }

    @Test
    void testAggiuntaVariabiliSintetiche(){
        Fraction[][] tableau = new Fraction[][]{
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO},
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO},
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO}
        };
        Value[][] nuovoTableau = DueFasi.aggiungiVariabiliSintetiche(tableau).tableau();
        Value[][] tableauExp = new Value[][]{
            {Value.ZERO,Value.ZERO,Value.ZERO,Value.ONE,Value.ONE,Value.ZERO},
            {Value.ONE,Value.ONE,Value.ONE,Value.ONE,Value.ZERO,Value.ZERO},
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO,Value.ONE,Value.ZERO}
        };

        assertEqualsT(tableauExp, nuovoTableau);
    }
    @Test
    void testAggiuntaVariabiliSintetiche2(){
        Fraction[][] tableau = new Fraction[][]{
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO},
            {Value.ZERO,Value.ONE,Value.ONE,Value.ZERO},
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO}
        };
        Value[][] nuovoTableau = DueFasi.aggiungiVariabiliSintetiche(tableau).tableau();
        Value[][] tableauExp = new Value[][]{
            {Value.ZERO,Value.ZERO,Value.ZERO,Value.ONE,Value.ZERO},
            {Value.ZERO,Value.ONE,Value.ONE,Value.ONE,Value.ZERO},
            {Value.ONE,Value.ONE,Value.ONE,Value.ZERO,Value.ZERO}
        };

        assertEqualsT(tableauExp, nuovoTableau);
    }

    @Test
    void testApplicaMetodoDueFasi(){
        Value[][] tableau = new Value[][]{
            {Value.ONE,Value.ZERO,Value.ZERO,Value.ZERO,Value.ZERO},
            {Value.ZERO,Value.ONE,new Fraction(2),new Fraction(3),new Fraction(15)},
            {Value.ZERO,new Fraction(2),new Fraction(1),new Fraction(5),new Fraction(20)},
            {Value.ONE,Value.ONE,new Fraction(2),new Fraction(1),new Fraction(10)},
        };

        Value[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());
        Value[][] expected = new Value[][] {
            {Value.ONE, Value.ZERO, Value.ZERO, Value.ZERO, Value.ZERO},
            {Fraction.of(-1,2), Value.ZERO, Value.ZERO, Value.ONE, Fraction.of(5, 2)},
            {Fraction.of(7,6), Value.ONE, Value.ZERO,Value.ZERO, Fraction.of(5, 2)},
            {Fraction.of(1,6), Value.ZERO, Value.ONE, Value.ZERO, Fraction.of(5,2)}
        };

        assertEqualsT(expected, out);
    }
    @Test
    void testApplicaMetodoDueFasi2(){
        Fraction[][] tableau = new Fraction[][]{
            {Value.ONE,Value.ZERO,Value.ONE,Value.ZERO,Value.ZERO},

            {Value.ONE, new Fraction(2),Value.ZERO,Value.ONE,new Fraction(5)},
            {Value.ZERO,Value.ONE,Fraction.of(2),Value.ZERO,new Fraction(6)},

        };

        Value[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());

        Value[][] tableauExp = new Value[][]{
            {Fraction.of(5,4),Value.ZERO,Value.ZERO,Fraction.of(1,4),Fraction.of(-7,4)},
            {Fraction.of(1,2), Value.ONE, Value.ZERO, Fraction.of(1,2),Fraction.of(5,2)},
            {Fraction.of(-1,4),Value.ZERO,Value.ONE,Fraction.of(-1,4),Fraction.of(7,4)}
        };

        assertEqualsT(tableauExp, out);
    }


    @Test
    void testApplicaMetodoDueFasiDegenere(){
        // Fase 1 degenere
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-3),Fraction.of(-2),Fraction.of(-1),Value.ZERO,Value.ZERO, Value.ZERO},

            {Fraction.of(2), Value.ONE, Value.ONE, Value.ONE, Value.ZERO, Fraction.of(2)},
            {Fraction.of(3), Fraction.of(4), Fraction.of(2), Value.ZERO, Value.MINUS_ONE, Fraction.of(8)}
        };

        Value[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());

        Value[][] tableauExp = new Value[][]{
            {Value.ONE,Value.ZERO,Value.ONE,Fraction.of(2),Value.ZERO,Fraction.of(4)},
            {Fraction.of(2), Value.ONE, Value.ONE, Value.ONE, Value.ZERO, Fraction.of(2)},
            {Fraction.of(5), Value.ZERO, Fraction.of(2), Fraction.of(4), Fraction.of(1)}
        };

        assertEquals(Fraction.of(4), out[0][out[0].length - 1]);
        assertEqualsT(tableauExp,out);
    }

    @Test
    void dueFasiVincoloRidondante(){
        Value[][] tableau = new Fraction[][]{
            {Fraction.of(-5), Fraction.of(-4), Fraction.of(-1), Fraction.of(-1), Value.ZERO},
            {Value.ONE, Fraction.of(2), Value.ONE, Value.ZERO, Fraction.of(10)},
            {Fraction.of(5), Fraction.of(5), Fraction.of(2), Value.ONE, Fraction.of(30)},
            {Fraction.of(3), Value.ONE, Value.ZERO, Value.ONE, Fraction.of(10)}
        };
        Value[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());

        Value[][] tableauExpected = new Value[][]{
            {Value.ZERO, Value.ZERO, Fraction.of(2,5), Fraction.of(1,5), Fraction.of(26)},
            {Value.ZERO, Value.ONE, Fraction.of(3,5), Fraction.of(-1,5), Fraction.of(4)},
            {Value.ONE, Value.ZERO, Fraction.of(-1,5), Fraction.of(2,5), Fraction.of(2)}
        };

        assertEqualsT(tableauExpected, out);
    }

    @Test
    void testProblemaIllimitato(){
        Fraction[][] tableau = new Fraction[][]{
            {Fraction.of(-4), Value.MINUS_ONE, Value.ZERO, Value.ZERO, Value.ZERO},
            {Value.MINUS_ONE, Value.ONE, Value.ONE, Value.ZERO},
            {Fraction.of(3), Fraction.of(-4), Value.ZERO, Value.ONE, Value.ZERO}
        };
        try {
            Value[][] out = DueFasi.applicaMetodoDueFasi(tableau, Collections.emptyList());
            
        } catch (RuntimeException e) {
            // TODO: handle exception
        }catch (Exception e) {
            fail();
        }
    }

}
