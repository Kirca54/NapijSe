package mk.napijse.model.exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String token_is_not_valid) {
        super("Invalid token exception");
    }
}
