package mk.napijse.model.exceptions;

public class RecipeAlreadyInFavouritesException extends RuntimeException{
    public RecipeAlreadyInFavouritesException(){
        super("Recipe is already added to favourites");
    }
}
