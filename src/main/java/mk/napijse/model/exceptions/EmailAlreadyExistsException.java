package mk.napijse.model.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(){
        super("Email already exists");
    }
}
