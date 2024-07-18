package it.naddeil.ro.common.utils;


public class Fraction implements  Value {
    public static Value of(long numerator, long denominator) {
        return new Fraction(numerator, denominator);
    }

    public static Fraction of(long numerator) {
        return new Fraction(numerator);
    }

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
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    private final long numerator;
    private final long denominator;

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

    @Override
    public Fraction floor(){
        if (numerator >= 0) {
            return new Fraction(numerator / denominator);
        }else{
            return new Fraction(numerator / denominator - 1);
        }}

    public Value add(Fraction other) {
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

    @Override
    public Fraction negate() {
        return new Fraction(-this.numerator, this.denominator);
    }

    @Override
    public Fraction abs() {
        return new Fraction(Math.abs(this.numerator), this.denominator);
    }

    @Override
    public boolean isInteger() {
        return denominator == 1;
    }

    @Override
    public double doubleValue(){
        return ((double)numerator)/denominator;
    }
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

    @Override
    public int compareTo(Value other) {
        if(other instanceof Fraction f){
            return this.compareTo(f);
        }
        throw new IllegalArgumentException("Not a Fraction");
    }

    @Override
    public Value add(Value other) {
        if(other instanceof Fraction f){
            return this.add(f);
        }
        return other.add(this);
    }

    @Override
    public Value subtract(Value other) {
        if(other instanceof Fraction f){
            return this.subtract(f);
        }
        return other.subtract(this).negate();
    }

    @Override
    public Value multiply(Value other) {
        if(other instanceof Fraction f){
            return this.multiply(f);
        }
        return other.multiply(this);
    }

    @Override
    public Value divide(Value other) {
        if(other instanceof Fraction f){
            return this.divide(f);
        }
        return other.divide(this);
    }
}