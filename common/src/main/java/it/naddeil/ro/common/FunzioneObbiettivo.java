package it.naddeil.ro.common;

import java.util.List;


public interface FunzioneObbiettivo {

    Tipo getTipo();

    void setTipo(Tipo tipo);

    List<Double> getC();

    void setC(List<Double> c);

    FunzioneObbiettivo copy();
} 