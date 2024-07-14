package it.naddeil.ro.common.models;

import java.util.List;
import java.util.stream.IntStream;

import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.TableauUtils;

import java.util.Arrays;
public class FracResult {
    Fraction[][] tableau;
    Fraction[] soluzione;
    Fraction[] costiRidotti;
    Fraction z;
    Fraction[][] a;
    List<Integer> basis;
    List<Fraction> valoreVariabili;

	private FracResult(Fraction[][] tableau, Fraction[] soluzione, Fraction[] costiRidotti, Fraction z, Fraction[][] a,
			List<Integer> basis, List<Fraction> valoreVariabili) {
		this.tableau = tableau;
		this.soluzione = soluzione;
		this.costiRidotti = costiRidotti;
		this.z = z;
		this.a = a;
		this.basis = basis;
        this.valoreVariabili = valoreVariabili;
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
        List<Fraction> valoreVariabili = TableauUtils.getSolution(costiRidotti.length, soluzione.length, tableau);
        return new FracResult(tableau, soluzione, costiRidotti, z, a, null, valoreVariabili);
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

    Fraction[] shiftRight(Fraction[] in)
    {
        Fraction[] out = in.clone();
        for (int i = 0; i < in.length; i++) {
            out[(i + 1) % in.length] = in[i];
        }

        return out;
        
    }

    public List<List<String>> getTableauStr(){
        return Arrays.stream(tableau).map(this::shiftRight).map(row -> Arrays.stream(row).map(Fraction::toString).toList()).toList();
    }
    public static void main(String[] args) {
        FracResult f = FracResult.fromTableau(new Fraction[][] {{Fraction.ONE, Fraction.ZERO,Fraction.ZERO}});
    
        var a = f.getTableauStr();

        int x = 1;
    }

    public List<Fraction> getValoreVariabili() {
        return valoreVariabili;
    }

    public void setValoreVariabili(List<Fraction> valoreVariabili) {
        this.valoreVariabili = valoreVariabili;
    }
}
