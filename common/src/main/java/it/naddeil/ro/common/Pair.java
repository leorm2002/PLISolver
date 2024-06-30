package it.naddeil.ro.common;

public class Pair {
    private final int i;
    private final int j;

    public Pair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    } 
}
