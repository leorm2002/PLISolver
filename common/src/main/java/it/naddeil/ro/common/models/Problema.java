package it.naddeil.ro.common.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import it.naddeil.ro.common.api.FunzioneObbiettivo;
import it.naddeil.ro.common.api.PublicProblem;
import it.naddeil.ro.common.api.Vincolo;
import it.naddeil.ro.common.utils.Fraction;
import it.naddeil.ro.common.utils.Value;

/**
 * Rappresenta un problema di ottimizzazione ovvero min F s.t. Ax=d dove ogni riga della matrice rappresenta un vincolo di tipo =
 */
public class Problema implements Serializable {
    private FunzioneObbiettivo funzioneObbiettivo;
    private List<Vincolo> vincoli;

    private SimpleMatrix a;
    private SimpleMatrix b;
    private SimpleMatrix c;
    private List<Integer> basis = new ArrayList<>();


	private Problema() {
	}

    public static Problema fromPublic(PublicProblem p){
        Problema out = new Problema();
        out.setFunzioneObbiettivo(p.getFunzioneObbiettivo());
        out.setVincoli(p.getVincoli());
        return out;
    }

    public Problema init(){
        if(funzioneObbiettivo == null || vincoli == null){
            return this;
        }
        int n = funzioneObbiettivo.getC().size();
        this.a = new SimpleMatrix(vincoli.size(), n);
        this.b  = new SimpleMatrix(vincoli.size(), 1);
        int j = 0;
        for (Vincolo vincolo : vincoli) {
            List<Double> coefficienti = vincolo.getVincolo();
            for (int i = 0; i < n; i++) {
                a.set(j, i, coefficienti.get(i));
            }
            b.set(j, 0, coefficienti.get(n));
            j++;
        }

        this.c = new SimpleMatrix(1, funzioneObbiettivo.getC().size());
        for (int i = 0; i < n; i++) {
            c.set(0, i, funzioneObbiettivo.getC().get(i));
        }

        return this;
    }

    public Fraction[][] toTableauFormProblem(){
        init(); 
        int numeroVincoli = this.a.numRows();
        int numeroVariabili = a.numCols();

        Fraction[][] tab = new Fraction[numeroVincoli + 1][numeroVariabili + 1];
        // Riporto funzione obbiettivo
        for(int i = 0; i < numeroVariabili; i++ ){
            tab[0][i] = Fraction.of(c.get(i));
        }
        tab[0][tab[0].length - 1] = Value.ZERO;
        // Riporto b
        for(int j = 0; j < numeroVincoli; j++){
            tab[j + 1][numeroVariabili] = Fraction.of(b.get(j));
        }

        // Riporto A
        for(int i = 0; i < numeroVincoli; i++ ){
            for(int j = 0; j < numeroVariabili; j++){
                tab[i + 1][j] = Fraction.of(a.get(i,j));
            }
        }
        return tab;
    }

	public FunzioneObbiettivo getFunzioneObbiettivo() {
		return funzioneObbiettivo;
	}

	private void setFunzioneObbiettivo(FunzioneObbiettivo funzioneObbiettivo) {
		this.funzioneObbiettivo = funzioneObbiettivo;
	}

	public List<Vincolo> getVincoli() {
		return vincoli;
	}

	private void setVincoli(List<Vincolo> vincoli) {
		this.vincoli = vincoli;
	}

	public SimpleMatrix getA() {
		return a;
	}

	public SimpleMatrix getB() {
		return b;
	}

	public SimpleMatrix getC() {
		return c;
	}

    public void setA(SimpleMatrix a) {
		this.a = a;
	}

	public void setB(SimpleMatrix b) {
		this.b = b;
	}

	public void setC(SimpleMatrix c) {
		this.c = c;
	}

	public List<Integer> getBasis() {
		return basis;
	}

}
