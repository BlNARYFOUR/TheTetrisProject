package util;

/**
 * Catch any exception when it is thrown by some matchable method,
 * and throw a MatchableException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class MatchableException extends RuntimeException {
    public MatchableException(String msg, Exception innerException) {
        super(msg, innerException);
    }

    public MatchableException(String msg) {
        super(msg);
    }
}
