package it.naddeil.ro.common;


public class ConfrontationUtils {
    public static boolean areEqual(double d1, double d2, double doubleTolerance) {
        return d1 == d2 || Math.abs(d1 - d2) < doubleTolerance;
    }
}