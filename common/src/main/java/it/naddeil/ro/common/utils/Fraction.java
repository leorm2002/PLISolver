package it.naddeil.ro.common.utils;


public class Fraction implements Comparable<Fraction> {
    private final long numerator;
    private final long denominator;

    public static Fraction of(long numerator, long denominator) {
        return new Fraction(numerator, denominator);
    }
    public static Fraction of(long numerator) {
        return new Fraction(numerator);
    }

    public long getNumerator() {
        return numerator;
    }

    public long getDenominator() {
        return denominator;
    }

    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction MINUS_ONE = new Fraction(-1, 1);
    public static final Fraction POSITIVE_INFINITY = new Fraction(1, 0);

    
    public static Fraction of(double value){
        boolean invert = value < 0;
        if (invert) {
            value = value * -1;
        }
        long denominator = 1;
        while (value != Math.floor(value)) {
            value *= 10;
            denominator *= 10;
        }
        if(invert){
            value = value * -1;
        }
        return new Fraction((long)value, denominator);
    }
    

    public Fraction(long numerator, long denominator) {
        if (denominator == 0 && numerator != 1) {
            throw new ArithmeticException("Denominatore zero non valido");
        }
        long gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = (denominator < 0 ? -1 : 1) * numerator / gcd;
        this.denominator = Math.abs(denominator) / gcd;
    }

    public Fraction(long wholeNumber) {
        this(wholeNumber, 1);
    }

    
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public Fraction floor(){
        if (numerator >= 0) {
            return new Fraction(numerator / denominator);
        }else{
            return new Fraction(numerator / denominator - 1);
        }}
    
    public Fraction add(Fraction other) {
        long newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        long newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction subtract(Fraction other) {
        long newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
        long newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        return new Fraction(this.numerator * other.denominator, this.denominator * other.numerator);
    }

    public Fraction negate() {
        return new Fraction(-this.numerator, this.denominator);
    }

    public Fraction abs() {
        return new Fraction(Math.abs(this.numerator), this.denominator);
    }

    public boolean isInteger() {
        return denominator == 1;
    }

    public double doubleValue(){
        return ((double)numerator)/denominator;
    }

    @Override
    public int compareTo(Fraction other) {
        long difference = this.numerator * other.denominator - other.numerator * this.denominator;
        return Long.compare(difference, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Fraction)) return false;
        Fraction other = (Fraction) obj;
        return this.numerator == other.numerator && this.denominator == other.denominator;
    }

    @Override
    public String toString() {
        if (denominator == 1) return Long.toString(numerator);
        return numerator + "/" + denominator;
    }
}