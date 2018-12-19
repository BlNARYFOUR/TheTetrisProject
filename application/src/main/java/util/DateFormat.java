package util;

/**
 * Date-formats are added in an accessible way.
 */
public enum DateFormat {
    YODA_TIME("yyyy-MM-dd HH:mm:ss");

    private final String format;

    DateFormat(final String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return format;
    }
}
