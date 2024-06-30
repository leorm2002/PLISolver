package it.naddeil.ro.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Rappresenta un problema di ottimizzazione ovvero min F s.t. Ax=d dove ogni riga della matrice rappresenta un vincolo di tipo =
 */
public class Problema implements Serializable {
    private FunzioneObbiettivo funzioneObbiettivo;
    private List<Vincolo> vincoli;


    Problema copy(){
        return null;
    }

    @JsonIgnore
    public StdProblem toMatrixProblem(){
        return null;
    }

	public FunzioneObbiettivo getFunzioneObbiettivo() {
		return funzioneObbiettivo;
	}

	public void setFunzioneObbiettivo(FunzioneObbiettivo funzioneObbiettivo) {
		this.funzioneObbiettivo = funzioneObbiettivo;
	}

	public List<Vincolo> getVincoli() {
		return vincoli;
	}

	public void setVincoli(List<Vincolo> vincoli) {
		this.vincoli = vincoli;
	}
}
