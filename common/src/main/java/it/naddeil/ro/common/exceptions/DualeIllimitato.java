package it.naddeil.ro.common.exceptions;

import java.util.List;

import it.naddeil.ro.common.api.Message;

public class DualeIllimitato extends BaseException {
    public DualeIllimitato(List<Message> state) {
        super("Il duale è illimitato, non esiste soluzione", state);
    }
}
