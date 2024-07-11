package it.naddeil.ro.simplexsolver;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import it.naddeil.ro.common.utils.Fraction;

class SimplexTableauTest {
  @Test
  void test1(){
    Fraction[] objectiveFunction = new Fraction[]{new Fraction(-200), new Fraction(-300), Fraction.ZERO, Fraction.ZERO};
    Fraction[][] constraints = new Fraction[][]{
      {Fraction.ONE, new Fraction(2), Fraction.ONE, Fraction.ZERO},
      {new Fraction(3), Fraction.ONE, Fraction.ZERO,Fraction.ONE}
    };
    Fraction[] rightHandSide = new Fraction[]{new Fraction(80), new Fraction(90)};
    SimplexTableau st = new SimplexTableau(objectiveFunction, constraints, rightHandSide);
    st.solve(false);
    Fraction[][] tableau = st.getTableau();
    assertEquals(new Fraction(13000), tableau[0][tableau[0].length -1]);
  }
}
