package util;

/**
 * Catch any exception when it is thrown by some highScore method,
 * and throw a HighScoreException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class HighScoreException extends RuntimeException {
    public HighScoreException(final String msg, final Exception innerException) {
        super(msg, innerException);
    }

    public HighScoreException(final String msg) {
        super(msg);
    }
}