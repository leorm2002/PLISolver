package it.naddeil.ro.common.exceptions;

import java.util.List;

import it.naddeil.ro.common.api.Message;

public class ProblemaAssociatoIrrisolvibile extends BaseException{
    public ProblemaAssociatoIrrisolvibile(List<Message> state) {
        super("Il problema associato è irrisolvibile", state);
    }
}
