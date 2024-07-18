package it.naddeil.ro.simplexsolver;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Value;

class SimplexTableauTest {
  @Test
  void test1(){
    Fraction[] objectiveFunction = new Fraction[]{new Fraction(-200), new Fraction(-300), Value.ZERO, Value.ZERO};
    Fraction[][] constraints = new Fraction[][]{
      {Value.ONE, new Fraction(2), Value.ONE, Value.ZERO},
      {new Fraction(3), Value.ONE, Value.ZERO,Value.ONE}
    };
    Fraction[] rightHandSide = new Fraction[]{new Fraction(80), new Fraction(90)};
    SimplexTableau st = new SimplexTableau(objectiveFunction, constraints, rightHandSide);
    st.solve(0);
    Value[][] tableau = st.getTableau();
    assertEquals(new Fraction(13000), tableau[0][tableau[0].length -1]);
  }
}
