package it.naddeil.ro.common.utils;

public class FloatingPoint implements Value {
    private double value;

    public static FloatingPoint of(double value) {
        return new FloatingPoint(value);
    }

    public FloatingPoint(double value) {
        this.value = value;
    }

    public static boolean areEqual(double d1, double d2, double doubleTolerance) {
        return d1 == d2 || Math.abs(d1 - d2) < doubleTolerance;
    }

    @Override
    public Value floor() {
        return new FloatingPoint(Math.floor(value));
    }

    private static double getDoubleValue(Value other) {
        if (other instanceof FloatingPoint) {
            return ((FloatingPoint) other).value;
        } else if (other instanceof Fraction) {
            return ((Fraction) other).doubleValue();
        } else {
            throw new UnsupportedOperationException("Unimplemented method 'getDoubleValue'");
        }
    }

    @Override
    public Value add(Value other) {
        return new FloatingPoint(value + getDoubleValue(other));
    }

    @Override
    public Value subtract(Value other) {
        return new FloatingPoint(value - getDoubleValue(other));
    }

    @Override
    public Value multiply(Value other) {
        return new FloatingPoint(value * getDoubleValue(other));
    }

    @Override
    public Value divide(Value other) {
        return new FloatingPoint(value / getDoubleValue(other));
    }

    @Override
    public Value negate() {
        return new FloatingPoint(-value);
    }

    @Override
    public Value abs() {
        return new FloatingPoint(Math.abs(value));
    }

    final static double EPSILON = 1e-8;

    @Override
    public boolean isInteger() {
        return areEqual(value % 1, 0, EPSILON) || areEqual(value % 1, 1, EPSILON);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int compareTo(Value other) {
        return Double.compare(value, getDoubleValue(other));
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Value) {
            return areEqual(value, getDoubleValue((Value) obj), EPSILON);
        }
        return false;
    }

}
