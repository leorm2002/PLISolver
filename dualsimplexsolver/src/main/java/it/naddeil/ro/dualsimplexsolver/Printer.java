package it.naddeil.ro.dualsimplexsolver;

public class Printer {

    public void printTableauFractions( double[][] tableau) {
        for (double[] row : tableau) {
            for (double val : row) {
                System.out.print(toFractionString(val) + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String toFractionString(double value) {
        if (value == 0) return "0";
        if (value == (int)value) return String.valueOf((int)value);

        Fraction fraction = new Fraction(value);
        return fraction.toString();
    }

    private static class Fraction {
        private final long numerator;
        private final long denominator;

        public Fraction(double value) {
            String s = String.valueOf(value);
            int digitsDec = s.length() - 1 - s.indexOf('.');
            long denom = 1;
            for (int i = 0; i < digitsDec; i++) {
                value *= 10;
                denom *= 10;
            }
            long num = Math.round(value);
            long g = gcd(Math.abs(num), denom);
            this.numerator = num / g;
            this.denominator = denom / g;
        }

        private static long gcd(long a, long b) {
            return b == 0 ? a : gcd(b, a % b);
        }

        @Override
        public String toString() {
            if (denominator == 1) return Long.toString(numerator);
            return numerator + "/" + denominator;
        }
    }

}
