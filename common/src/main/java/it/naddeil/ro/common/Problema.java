package it.naddeil.ro.common;

import java.util.List;

/**
 * Rappresenta un problema di ottimizzazione ovvero min F s.t. Ax=d dove ogni riga della matrice rappresenta un vincolo di tipo =
 */
public interface Problema {
    FunzioneObbiettivo getFunzioneObbiettivo();

    void setFunzioneObbiettivo(FunzioneObbiettivo funzioneObbiettivo);

    List<Vincolo> getVincoli();

    void setVincoli(List<Vincolo> vincoli);

    Problema copy();

    // TODO jsonignore
    MatrixProblem toMatrixProblem();
}
