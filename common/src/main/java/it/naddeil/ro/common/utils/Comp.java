package it.naddeil.ro.common.utils;

public interface Comp extends Comparable<Comp> {

    Fraction ZERO = new Fraction(0, 1);
    Fraction ONE = new Fraction(1, 1);
    Fraction MINUS_ONE = new Fraction(-1, 1);
    Fraction POSITIVE_INFINITY = new Fraction(1, 0);

    Comp floor();

    Comp add(Comp other);

    Comp subtract(Comp other);

    Comp multiply(Comp other);

    Comp divide(Comp other);

    Comp negate();

    Comp abs();

    boolean isInteger();

    double doubleValue();

    int compareTo(Comp other);

    boolean equals(Object obj);

    String toString();

}