package mk.napijse.model.exceptions;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(){
        super("The email must contain - @");
    }
}
