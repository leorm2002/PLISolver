package it.naddeil.ro.common.exceptions;

import java.util.List;
import it.naddeil.ro.common.api.Message;

public class ProblemaInizialeIllimitato extends BaseException{
    public ProblemaInizialeIllimitato(List<Message> state) {
        super("Il problema iniziale è illimitato", state);
    }
}
