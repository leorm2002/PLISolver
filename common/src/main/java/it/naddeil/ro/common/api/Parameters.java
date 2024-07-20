package it.naddeil.ro.common.api;

import java.io.Serializable;

public class Parameters implements Serializable {
    private Integer maxIterazioni = 30;
    private Boolean passaggiIntermedi = true;


    public Boolean getPassaggiIntermedi() {
        return passaggiIntermedi;
    }

    public void setPassaggiIntermedi(Boolean passaggiIntermedi) {
        this.passaggiIntermedi = passaggiIntermedi;
    }

    public Integer getMaxIterazioni() {
        return maxIterazioni;
    }

    public void setMaxIterazioni(Integer maxIterazioni) {
        this.maxIterazioni = maxIterazioni;
    }
}
