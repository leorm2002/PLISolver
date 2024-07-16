package it.naddeil.ro.common.models;

import java.util.List;

import it.naddeil.ro.common.api.Message;
import it.naddeil.ro.common.utils.Fraction;

public class FracResultWithPassages extends FracResult {
    private List<Message> passaggi;
    FracResultWithPassages(Fraction[][] tableau, Fraction[] soluzione, Fraction[] costiRidotti, Fraction z,
            Fraction[][] a, List<Integer> basis, List<Fraction> valoreVariabili, List<Message> passaggi) {
        super(tableau, soluzione, costiRidotti, z, a, basis, valoreVariabili);
        this.passaggi = passaggi;
    }

    public static FracResultWithPassages fromTableau(Fraction[][] tableau, List<Message> passaggi) {
        return FracResultWithPassages.fromTableau(tableau, false, passaggi);

    }
    public static FracResultWithPassages fromTableau(Fraction[][] tableau, boolean invert, List<Message> passaggi) {
        FracResult res = fromTableau(tableau, invert);
        return new FracResultWithPassages(tableau, res.getSoluzione(), res.getCostiRidotti(), res.getZ(), res.getA(), res.getBasis(), res.getValoreVariabili(), passaggi);
    }

    public List<Message> getPassaggi() {
        return passaggi;
    }
}
