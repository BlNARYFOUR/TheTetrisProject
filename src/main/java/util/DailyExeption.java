package util;

/**
 * javadoc.
 */
public class DailyExeption extends RuntimeException {
    public DailyExeption(final String msg, final Exception innerException) {
        super(msg, innerException);
    }
}
