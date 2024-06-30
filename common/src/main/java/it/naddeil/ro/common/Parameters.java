package it.naddeil.ro.common;

public class Parameters {
    private double tollleranza = 0.00000000001;
    private int maxIterazioni = 30;

	public double getTollleranza() {
		return tollleranza;
	}
	public void setTollleranza(double tollleranza) {
		this.tollleranza = tollleranza;
	}
	public int getMaxIterazioni() {
		return maxIterazioni;
	}
	public void setMaxIterazioni(int maxIterazioni) {
		this.maxIterazioni = maxIterazioni;
	}
}
