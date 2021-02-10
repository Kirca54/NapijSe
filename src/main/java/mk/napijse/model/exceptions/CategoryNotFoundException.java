package mk.napijse.model.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){
        super("Category not found exception");
    }
}
