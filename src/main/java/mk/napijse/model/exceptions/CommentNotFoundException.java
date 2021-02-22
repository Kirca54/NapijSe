package mk.napijse.model.exceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(){
        super("Comment not found");
    }
}
