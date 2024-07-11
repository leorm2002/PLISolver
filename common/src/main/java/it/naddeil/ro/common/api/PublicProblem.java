package it.naddeil.ro.common.api;

import java.io.Serializable;
import java.util.List;

/**
 * Rappresenta un problema di ottimizzazione ovvero min F s.t. Ax=d dove ogni riga della matrice rappresenta un vincolo di tipo =
 */
public class PublicProblem implements Serializable {
    private FunzioneObbiettivo funzioneObbiettivo;
    private List<Vincolo> vincoli;
    

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
    @Override
    public String toString() {
        return "PublicProblem [funzioneObbiettivo=" + funzioneObbiettivo + ", vincoli=" + vincoli + "]";
    }

   
}
