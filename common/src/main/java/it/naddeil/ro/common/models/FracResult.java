package it.naddeil.ro.common.models;

import java.util.List;
import java.util.stream.IntStream;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.TableauUtils;
import it.naddeil.ro.common.utils.Value;

import java.util.Arrays;
public class FracResult {
    Value[][] tableau;
    Value[] soluzione;
    Value[] costiRidotti;
    Value z;
    Value[][] a;
    List<Integer> basis;
    List<Value> valoreVariabili;
    List<Message> out;

	public List<Message> getOut() {
        return out;
    }

    public FracResult setOut(List<Message> out) {
        this.out = out;
        return this;
    }

    FracResult(Value[][] tableau, Value[] soluzione, Value[] costiRidotti, Value z, Value[][] a,
			List<Integer> basis, List<Value> valoreVariabili) {
		this.tableau = tableau;
		this.soluzione = soluzione;
		this.costiRidotti = costiRidotti;
		this.z = z;
		this.a = a;
		this.basis = basis;
        this.valoreVariabili = valoreVariabili;
	}

    public static FracResult fromTableau(Value[][] tableau, boolean invert){
        int width = tableau[0].length;
        Value[] soluzione = IntStream.range(1, tableau.length).mapToObj(i -> tableau[i][width -1]).toArray(Value[]::new);
        Value[] costiRidotti = IntStream.range(0, width - 1).mapToObj(i -> tableau[0][i]).toArray(Value[]::new);
        Value z = tableau[0][width - 1];
        z = invert ? z.negate() : z;
        Value[][] a = new Value[tableau.length - 1][width - 1];
		for (int i = 1; i < tableau.length; i++) {
			a[i - 1] = Arrays.copyOf(tableau[i], width - 1);
		}
        List<Value> valoreVariabili = TableauUtils.getSolution(costiRidotti.length, soluzione.length, tableau);
        return new FracResult(tableau, soluzione, costiRidotti, z, a, null, valoreVariabili);
    }
    public static FracResult fromTableau(Value[][] tableau){
        return fromTableau(tableau, false);
    }
	
    public Value[][] getTableau() {
		return tableau;
	}
	
    public void setTableau(Value[][] tableau) {
		this.tableau = tableau;
	}
	public Value[] getSoluzione() {
		return soluzione;
	}
	public void setSoluzione(Value[] soluzione) {
		this.soluzione = soluzione;
	}
	public Value[] getCostiRidotti() {
		return costiRidotti;
	}
	public void setCostiRidotti(Value[] costiRidotti) {
		this.costiRidotti = costiRidotti;
	}
	public Value getZ() {
		return z;
	}
	public void setZ(Value z) {
		this.z = z;
	}
	public Value[][] getA() {
		return a;
	}
	public void setA(Value[][] a) {
		this.a = a;
	}
	public List<Integer> getBasis() {
		return basis;
	}
	public void setBasis(List<Integer> basis) {
		this.basis = basis;
	}

    Value[] shiftRight(Value[] in)
    {
        Value[] out = in.clone();
        for (int i = 0; i < in.length; i++) {
            out[(i + 1) % in.length] = in[i];
        }

        return out;
        
    }

    public List<List<String>> getTableauStr(){
        return Arrays.stream(tableau).map(this::shiftRight).map(row -> Arrays.stream(row).map(Value::toString).toList()).toList();
    }
    public static void main(String[] args) {
        FracResult f = FracResult.fromTableau(new Fraction[][] {{Value.ONE, Value.ZERO,Value.ZERO}});
    
        var a = f.getTableauStr();

        int x = 1;
    }

    public List<Value> getValoreVariabili() {
        return valoreVariabili;
    }

    public void setValoreVariabili(List<Value> valoreVariabili) {
        this.valoreVariabili = valoreVariabili;
    }
}
