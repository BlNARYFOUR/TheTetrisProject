package util;

/**
 * Date-formats are added in an accessible way.
 */
public enum DateFormat {
    YODA_TIME("yyyy-MM-dd hh:mm:ss");

    private final String format;

    DateFormat(String format) {
        this.format = format;
    }

    public String toString() {
        return format;
    }
}
