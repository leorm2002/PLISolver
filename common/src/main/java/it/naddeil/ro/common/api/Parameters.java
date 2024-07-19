package it.naddeil.ro.common.api;

public class Parameters {
    private int maxIterazioni = 30;
    private Boolean passaggiIntermedi = true;

    public int getMaxIterazioni() {
        return maxIterazioni;
    }

    public void setMaxIterazioni(int maxIterazioni) {
        this.maxIterazioni = maxIterazioni;
    }

    public Boolean getPassaggiIntermedi() {
        return passaggiIntermedi;
    }

    public void setPassaggiIntermedi(Boolean passaggiIntermedi) {
        this.passaggiIntermedi = passaggiIntermedi;
    }
}
