package it.naddeil.ro.common.api;

import java.io.Serializable;
import java.util.List;

import it.naddeil.ro.common.models.Result;
import it.naddeil.ro.common.utils.Value;

public class Message implements Serializable {
    String message;
    List<List<String>> tableau;
    List<Message> passaggiIntermedi;

    public Message(List<Message> passaggiIntermedi) {
        this.passaggiIntermedi = passaggiIntermedi;
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, List<List<String>> tableau) {
        this.message = message;
        this.tableau = tableau;
    }

    public static Message conPassaggiIntermedi(List<Message> passaggiIntermedi) {
        return new Message(passaggiIntermedi);
    }

    public static Message messaggioConTaglio(int riga, Value[] taglio) {
        return new Message("Calcolo da riga " + riga + "\nTaglio risultante :" + taglioToStr(taglio));
    }

    public static Message messaggioSemplice(String messaggio) {
        return new Message(messaggio);
    }

    public static Message messaggioConTableau(String messaggio, Value[][] tableau) {
        return new Message(messaggio, formattaTableauJson(tableau));
    }

    public static Message messaggioConRisultato(String messaggio, Result tableau) {
        return new Message(messaggio, formattaTableauJson(tableau.getTableau()));
    }

    private static List<List<String>> formattaTableauJson(Value[][] tableau) {
        return Result.fromTableau(tableau).getTableauStr();
    }

    static String taglioToStr(Value[] taglio) {
        int i = 1;
        String out = "";
        for (int j = 0; j < taglio.length - 1; j++) {
            out += taglio[j].toString() + " x" + i++ + " + ";
        }
        out += taglio[taglio.length - 2].toString();
        out += ">= " + taglio[taglio.length - 1].toString();

        return out;
    }

    @Override
    public String toString() {
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<List<String>> getTableau() {
        return tableau;
    }

    public void setTableau(List<List<String>> tableau) {
        this.tableau = tableau;
    }

    public List<Message> getPassaggiIntermedi() {
        return passaggiIntermedi;
    }
}
