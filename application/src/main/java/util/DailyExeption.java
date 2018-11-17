package util;

public class DailyExeption extends RuntimeException {
    public DailyExeption(String msg, Exception innerException){
        super(msg, innerException);
    }
}
