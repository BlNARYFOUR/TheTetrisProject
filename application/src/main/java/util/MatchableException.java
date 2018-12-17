package util;

/**
 * Catch any exception when it is thrown by some matchable method,
 * and throw a MatchableException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class MatchableException extends RuntimeException {
    public MatchableException(final String msg, final Exception innerException) {
        super(msg, innerException);
    }

    public MatchableException(final String msg) {
        super(msg);
    }
}
