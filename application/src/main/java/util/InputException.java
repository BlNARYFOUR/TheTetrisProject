package util;

/**
 * Catch any exception when it is thrown by some input method,
 * and throw a InputException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class InputException extends RuntimeException {
    public InputException(final String msg, final Exception innerException) {
        super(msg, innerException);
    }

    public InputException(final String msg) {
        super(msg);
    }
}
