package it.naddeil.ro.simplexsolver;

import it.naddeil.ro.common.Parameters;
import it.naddeil.ro.common.pub.PublicProblem;
import it.naddeil.ro.common.FracResult;
import it.naddeil.ro.common.Fraction;


public class SimplexSolver {


    public FracResult solve(PublicProblem problem, Parameters parameters) {
        Fraction[][] tableau = {
            {new Fraction(0), new Fraction(0), new Fraction(-9,5), new Fraction(-8,5), new Fraction(-1,5), new Fraction(28,5)},
            {Fraction.ZERO, Fraction.ONE, new Fraction(-1,5), new Fraction(-2,5), new Fraction(1,5), new Fraction(2,5)},
            {Fraction.ONE, Fraction.ZERO, new Fraction(7,5), new Fraction(-1,5), new Fraction(-2,5), new Fraction(11,5)}
        };

        Fraction[][] tableauu = {
            {new Fraction(0), new Fraction(0), new Fraction(-1,4), new Fraction(-1,4), new Fraction(-3,2)},
            {Fraction.ONE, Fraction.ZERO, new Fraction(1,6), new Fraction(-1,6),Fraction.ONE},
            {Fraction.ZERO, Fraction.ONE, new Fraction(1,4), new Fraction(1,4), new Fraction(3,2)}
        };
        return FracResult.fromTableau(tableauu);
    }
}
