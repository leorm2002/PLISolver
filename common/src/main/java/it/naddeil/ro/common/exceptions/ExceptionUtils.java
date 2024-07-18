package it.naddeil.ro.common.exceptions;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


import it.naddeil.ro.common.api.Message;

public class ExceptionUtils {
    public static void catchAndRetrow(Runnable solve, List<Message> passaggiPrec) {
        try {
            solve.run();
        } catch (BaseException e) {
            passaggiPrec.addAll(e.getState());
            e.setState(passaggiPrec);
            throw e;
        }
        }

    public static <T>  T catchAndRetrow(Supplier<T> solve, List<Message> passaggiPrec) {
            try {
                return solve.get();
            } catch (BaseException e) {
                passaggiPrec.addAll(e.getState());
                e.setState(passaggiPrec);
                throw e;
            }
            }
}
