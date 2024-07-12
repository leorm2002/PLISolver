package it.naddeil.ro.common.api;

import java.util.List;;
public class Responsee implements java.io.Serializable {
    public List<List<String>> tableau;
    public long time;
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

}
