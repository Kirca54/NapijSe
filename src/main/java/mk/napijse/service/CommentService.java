package mk.napijse.service;

import mk.napijse.model.Comment;
import mk.napijse.model.Recipe;
import mk.napijse.model.User;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();
    List<Comment> findAllByRecipe(Long recipeId);
    List<Comment> findAllByRecipeAndUsername(Long recipeId, String username);
    Comment save(Long recipeId, String username, String content);
}
