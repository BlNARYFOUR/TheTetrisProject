package util;

public class UserException extends RuntimeException {
    public UserException(String msg, Exception innerException){
        super(msg, innerException);
    }

    public UserException(String msg){
        super(msg);
    }
}
