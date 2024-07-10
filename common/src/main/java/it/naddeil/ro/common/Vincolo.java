package it.naddeil.ro.common;

import java.io.Serializable;
import java.util.List;


public class Vincolo implements Serializable {
    private List<Double> vincolo;
    private Verso verso;


    public Vincolo() {
    }

    public Vincolo(List<Double> vincolo, Verso verso) {
		this.vincolo = vincolo;
		this.verso = verso;
	}

	public List<Double> getVincolo() {
		return vincolo;
	}


	public void setVincolo(List<Double> vincolo) {
		this.vincolo = vincolo;
	}


	public Verso getVerso() {
		return verso;
	}


	public void setVerso(Verso verso) {
		this.verso = verso;
	}

    @Override
    public String toString() {
        return "Vincolo [vincolo=" + vincolo + ", verso=" + verso + "]";
    }
}
