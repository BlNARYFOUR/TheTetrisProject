package util;

public class LoginException extends RuntimeException {
    public LoginException(String msg, Exception innerException){
        super(msg, innerException);
    }

    public LoginException(String msg){
        super(msg);
    }
}
