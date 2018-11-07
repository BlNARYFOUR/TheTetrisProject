package util;

public class MatchableException extends RuntimeException {
    public MatchableException(String msg, Exception innerException){
        super(msg, innerException);
    }

    public MatchableException(String msg){
        super(msg);
    }
}
