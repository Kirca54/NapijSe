package mk.napijse.service;

import mk.napijse.model.entities.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAll();
    void deleteById(Long id);
    Optional<Recipe> findById(Long id);
    Recipe editRecipe(Long id, String name, String description, String ingredients, Long category);
    Recipe saveRecipe (String name, String description, String ingredients, Long category, String username);
    List<Recipe> findAllFavourites(String username);
    Recipe addToFavourites(String username, Long recipeId);
    void deleteFromFavourites(String username, Long recipeId);
    List<Recipe> findAllByName(String name);
    List<Recipe> findAllByCategory(Long categoryId);
    List<Recipe> findAllByNameAndCategory(String name, Long categoryId);
    List<Recipe> findAllByRecipeUser(String username);
}
