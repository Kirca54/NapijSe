package mk.napijse.model.exceptions;

public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException(){
        super("Recipe not found exception");
    }
}
