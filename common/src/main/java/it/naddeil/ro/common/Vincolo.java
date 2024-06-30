package it.naddeil.ro.common;

import java.util.List;


public interface Vincolo {
    List<Double> getVincolo();

    void setVincolo(List<Double> vincolo);

    Verso getVerso();

    void setVerso(Verso verso);

    Vincolo copy();
}
