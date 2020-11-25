package Model.Exceptions;

public class StatusException extends Exception{
    public StatusException(){
        super();
    }

    public StatusException(String message){
        super(message);
    }
    public StatusException(String message, Throwable throwable){
        super(message, throwable);
    }
}
