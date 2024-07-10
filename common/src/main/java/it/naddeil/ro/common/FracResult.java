package it.naddeil.ro.common;

import java.util.List;
import java.util.stream.IntStream;
import java.util.Arrays;
public class FracResult {
    Fraction[][] tableau;
    Fraction[] soluzione;
    Fraction[] costiRidotti;
    Fraction z;
    Fraction[][] a;
    List<Integer> basis;

	public FracResult(Fraction[][] tableau, Fraction[] soluzione, Fraction[] costiRidotti, Fraction z, Fraction[][] a,
			List<Integer> basis) {
		this.tableau = tableau;
		this.soluzione = soluzione;
		this.costiRidotti = costiRidotti;
		this.z = z;
		this.a = a;
		this.basis = basis;
	}

    public static FracResult fromTableau(Fraction[][] tableau){
        int width = tableau[0].length;
        Fraction[] soluzione = IntStream.range(1, tableau.length).mapToObj(i -> tableau[i][width -1]).toArray(Fraction[]::new);
        Fraction[] costiRidotti = IntStream.range(0, width - 1).mapToObj(i -> tableau[0][i]).toArray(Fraction[]::new);
        Fraction z = tableau[0][width - 1];
        Fraction[][] a = new Fraction[tableau.length - 1][width - 1];
		for (int i = 1; i < tableau.length; i++) {
			a[i - 1] = Arrays.copyOf(tableau[i], width - 1);
		}
        return new FracResult(tableau, soluzione, costiRidotti, z, a, null);
    }
	public Fraction[][] getTableau() {
		return tableau;
	}
	public void setTableau(Fraction[][] tableau) {
		this.tableau = tableau;
	}
	public Fraction[] getSoluzione() {
		return soluzione;
	}
	public void setSoluzione(Fraction[] soluzione) {
		this.soluzione = soluzione;
	}
	public Fraction[] getCostiRidotti() {
		return costiRidotti;
	}
	public void setCostiRidotti(Fraction[] costiRidotti) {
		this.costiRidotti = costiRidotti;
	}
	public Fraction getZ() {
		return z;
	}
	public void setZ(Fraction z) {
		this.z = z;
	}
	public Fraction[][] getA() {
		return a;
	}
	public void setA(Fraction[][] a) {
		this.a = a;
	}
	public List<Integer> getBasis() {
		return basis;
	}
	public void setBasis(List<Integer> basis) {
		this.basis = basis;
	}
}
