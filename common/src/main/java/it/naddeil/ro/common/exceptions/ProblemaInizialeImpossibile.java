package it.naddeil.ro.common.exceptions;

import java.util.List;
import it.naddeil.ro.common.api.Message;

public class ProblemaInizialeImpossibile extends BaseException{
    public ProblemaInizialeImpossibile(List<Message> state) {
        super("Il problema iniziale è impossibile", state);
    }
}
