package util;

/**
 * Catch any exception when it is thrown by some Daily method,
 * and throw a DailyException. This is useful to hide a potential vulnerability to users,
 * but give useful info to Developers.
 */
public class DailyException extends RuntimeException {
    public DailyException(String msg, Exception innerException) {
        super(msg, innerException);
    }
}
