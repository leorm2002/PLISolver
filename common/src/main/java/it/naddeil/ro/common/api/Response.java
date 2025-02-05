package it.naddeil.ro.common.api;

import java.util.List;;
public class Response implements java.io.Serializable {
    public List<List<String>> tableau;
    public List<String> soluzione;
    public long time;
    public List<Message> passiRisoluzione;
    public String error;
    public List<List<String>> getTableau() {
        return tableau;
    }
    public void setTableau(List<List<String>> tableau) {
        this.tableau = tableau;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public List<String> getSoluzione() {
        return soluzione;
    }
    public void setSoluzione(List<String> soluzione) {
        this.soluzione = soluzione;
    }
    public List<Message> getPassiRisoluzione() {
        return passiRisoluzione;
    }
    public void setPassiRisoluzione(List<Message> passiRisoluzione) {
        this.passiRisoluzione = passiRisoluzione;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

}
