package it.naddeil.ro.simplexsolver;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Comp;

class SimplexTableauTest {
  @Test
  void test1(){
    Fraction[] objectiveFunction = new Fraction[]{new Fraction(-200), new Fraction(-300), Comp.ZERO, Comp.ZERO};
    Fraction[][] constraints = new Fraction[][]{
      {Comp.ONE, new Fraction(2), Comp.ONE, Comp.ZERO},
      {new Fraction(3), Comp.ONE, Comp.ZERO,Comp.ONE}
    };
    Fraction[] rightHandSide = new Fraction[]{new Fraction(80), new Fraction(90)};
    SimplexTableau st = new SimplexTableau(objectiveFunction, constraints, rightHandSide);
    st.solve(0);
    Comp[][] tableau = st.getTableau();
    assertEquals(new Fraction(13000), tableau[0][tableau[0].length -1]);
  }
}
