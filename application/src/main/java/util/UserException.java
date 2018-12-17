package util;

/**
 * Catch any exception when it is thrown by some user method,
 * and throw a UserException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class UserException extends RuntimeException {
    public UserException(final String msg, final Exception innerException) {
        super(msg, innerException);
    }

    public UserException(final String msg) {
        super(msg);
    }
}
