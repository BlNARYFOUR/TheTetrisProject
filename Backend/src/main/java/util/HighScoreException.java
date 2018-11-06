package util;

public class HighScoreException extends RuntimeException {
    public HighScoreException(String msg, Exception innerException) {
        super(msg, innerException);
    }

    public HighScoreException(String msg) {
        super(msg);
    }
}
