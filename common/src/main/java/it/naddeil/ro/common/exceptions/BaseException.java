package it.naddeil.ro.common.exceptions;

import java.util.List;

import it.naddeil.ro.common.api.Message;

public class BaseException extends RuntimeException {
    private final List<Message> state;
    public BaseException(String message, List<Message> state) {
        super(message);
        this.state = state;
    }
    public List<Message> getState() {
        return state;
    }

    public void setState(List<Message> state) {
        state = state;
    }
}
