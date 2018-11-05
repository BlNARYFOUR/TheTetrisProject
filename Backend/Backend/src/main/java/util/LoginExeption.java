package util;

public class LoginExeption extends RuntimeException {
    public LoginExeption(String msg, Exception innerException){
        super(msg, innerException);
    }

    public LoginExeption(String msg){
        super(msg);
    }
}
