package it.naddeil.ro.common.exceptions;

import java.util.List;
import it.naddeil.ro.common.api.Message;

public class NumeroMassimoIterazioni extends BaseException{
    public NumeroMassimoIterazioni(List<Message> state) {
        super("Numero massimo di iterazioni raggiunto", state);
    }
}
