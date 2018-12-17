package util;

/**
 * Catch any exception when it is thrown by some hero method,
 * and throw a HeroException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class HeroException extends RuntimeException {
    public HeroException(final String msg, final Exception innerException) {
        super(msg, innerException);
    }

    public HeroException(final String msg) {
        super(msg);
    }
}
