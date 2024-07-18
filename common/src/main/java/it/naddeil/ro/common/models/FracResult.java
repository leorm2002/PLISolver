package it.naddeil.ro.common.models;

import java.util.List;
import java.util.stream.IntStream;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.TableauUtils;
import it.naddeil.ro.common.utils.Comp;

import java.util.Arrays;
public class FracResult {
    Comp[][] tableau;
    Comp[] soluzione;
    Comp[] costiRidotti;
    Comp z;
    Comp[][] a;
    List<Integer> basis;
    List<Comp> valoreVariabili;
    List<Message> out;

	public List<Message> getOut() {
        return out;
    }

    public FracResult setOut(List<Message> out) {
        this.out = out;
        return this;
    }

    FracResult(Comp[][] tableau, Comp[] soluzione, Comp[] costiRidotti, Comp z, Comp[][] a,
			List<Integer> basis, List<Comp> valoreVariabili) {
		this.tableau = tableau;
		this.soluzione = soluzione;
		this.costiRidotti = costiRidotti;
		this.z = z;
		this.a = a;
		this.basis = basis;
        this.valoreVariabili = valoreVariabili;
	}

    public static FracResult fromTableau(Comp[][] tableau, boolean invert){
        int width = tableau[0].length;
        Comp[] soluzione = IntStream.range(1, tableau.length).mapToObj(i -> tableau[i][width -1]).toArray(Comp[]::new);
        Comp[] costiRidotti = IntStream.range(0, width - 1).mapToObj(i -> tableau[0][i]).toArray(Comp[]::new);
        Comp z = tableau[0][width - 1];
        z = invert ? z.negate() : z;
        Comp[][] a = new Comp[tableau.length - 1][width - 1];
		for (int i = 1; i < tableau.length; i++) {
			a[i - 1] = Arrays.copyOf(tableau[i], width - 1);
		}
        List<Comp> valoreVariabili = TableauUtils.getSolution(costiRidotti.length, soluzione.length, tableau);
        return new FracResult(tableau, soluzione, costiRidotti, z, a, null, valoreVariabili);
    }
    public static FracResult fromTableau(Comp[][] tableau){
        return fromTableau(tableau, false);
    }
	
    public Comp[][] getTableau() {
		return tableau;
	}
	
    public void setTableau(Comp[][] tableau) {
		this.tableau = tableau;
	}
	public Comp[] getSoluzione() {
		return soluzione;
	}
	public void setSoluzione(Comp[] soluzione) {
		this.soluzione = soluzione;
	}
	public Comp[] getCostiRidotti() {
		return costiRidotti;
	}
	public void setCostiRidotti(Comp[] costiRidotti) {
		this.costiRidotti = costiRidotti;
	}
	public Comp getZ() {
		return z;
	}
	public void setZ(Comp z) {
		this.z = z;
	}
	public Comp[][] getA() {
		return a;
	}
	public void setA(Comp[][] a) {
		this.a = a;
	}
	public List<Integer> getBasis() {
		return basis;
	}
	public void setBasis(List<Integer> basis) {
		this.basis = basis;
	}

    Comp[] shiftRight(Comp[] in)
    {
        Comp[] out = in.clone();
        for (int i = 0; i < in.length; i++) {
            out[(i + 1) % in.length] = in[i];
        }

        return out;
        
    }

    public List<List<String>> getTableauStr(){
        return Arrays.stream(tableau).map(this::shiftRight).map(row -> Arrays.stream(row).map(Comp::toString).toList()).toList();
    }
    public static void main(String[] args) {
        FracResult f = FracResult.fromTableau(new Fraction[][] {{Comp.ONE, Comp.ZERO,Comp.ZERO}});
    
        var a = f.getTableauStr();

        int x = 1;
    }

    public List<Comp> getValoreVariabili() {
        return valoreVariabili;
    }

    public void setValoreVariabili(List<Comp> valoreVariabili) {
        this.valoreVariabili = valoreVariabili;
    }
}
