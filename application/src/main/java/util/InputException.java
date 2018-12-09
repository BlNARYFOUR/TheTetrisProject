package util;

public class InputException extends RuntimeException {
    public InputException(String msg, Exception innerException){
        super(msg, innerException);
    }

    public InputException(String msg){
        super(msg);
    }
}
