package it.naddeil.ro.common;

import java.io.Serializable;
import java.util.List;


public class FunzioneObbiettivo implements Serializable {
    Tipo tipo;
    List<Double> c;

    FunzioneObbiettivo copy(){
        return null;
    }

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public List<Double> getC() {
		return c;
	}

	public void setC(List<Double> c) {
		this.c = c;
	}
} 