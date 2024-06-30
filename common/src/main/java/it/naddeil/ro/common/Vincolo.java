package it.naddeil.ro.common;

import java.util.List;


public class Vincolo {
    private List<Double> vincolo;
    private Verso verso;

    
    public Vincolo(List<Double> vincolo, Verso verso) {
		this.vincolo = vincolo;
		this.verso = verso;
	}


	Vincolo copy(){
        return null;
    
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
}
