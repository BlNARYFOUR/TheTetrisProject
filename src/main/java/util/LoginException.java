package util;

/**
 * Catch any exception when it is thrown by some login method,
 * and throw a LoginException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class LoginException extends RuntimeException {
    public LoginException(final String msg, final Exception innerException) {
        super(msg, innerException);
    }

    public LoginException(final String msg) {
        super(msg);
    }
}
