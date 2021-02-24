package mk.napijse.service;

import mk.napijse.model.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();
    List<Comment> findAllByRecipe(Long recipeId);
    List<Comment> findAllByRecipeAndUsername(Long recipeId, String username);
    Comment save(Long recipeId, String username, String content);
    Comment edit(Long recipeId, Long commentId, String content);
    Comment findById(Long id);
    void delete(Long recipeId, Long commentId);
}
