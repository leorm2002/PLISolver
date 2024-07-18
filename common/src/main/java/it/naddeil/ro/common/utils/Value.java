package it.naddeil.ro.common.utils;

public interface Value extends Comparable<Value> {

    Fraction ZERO = new Fraction(0, 1);
    Fraction ONE = new Fraction(1, 1);
    Fraction MINUS_ONE = new Fraction(-1, 1);
    Fraction POSITIVE_INFINITY = new Fraction(1, 0);

    Value floor();

    Value add(Value other);

    Value subtract(Value other);

    Value multiply(Value other);

    Value divide(Value other);

    Value negate();

    Value abs();

    boolean isInteger();

    double doubleValue();

    int compareTo(Value other);

    boolean equals(Object obj);

    String toString();

}