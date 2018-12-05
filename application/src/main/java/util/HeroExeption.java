package util;

public class HeroExeption extends RuntimeException {
    public HeroExeption(String msg, Exception innerException){
        super(msg, innerException);
    }

    public HeroExeption(String msg){
        super(msg);
    }
}
