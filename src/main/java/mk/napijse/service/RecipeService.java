package mk.napijse.service;

import mk.napijse.model.entities.Recipe;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAll();
    Page<Recipe> findAllPaginated(int pageNumber, int pageSize);
    void deleteById(Long id);
    Optional<Recipe> findById(Long id);
    Recipe editRecipe(Long id, String name, String description, String ingredients, Long category);
    Recipe saveRecipe (String name, String description, String ingredients, Long category, String username);
    List<Recipe> findAllFavourites(String username);
    Recipe addToFavourites(String username, Long recipeId);
    void deleteFromFavourites(String username, Long recipeId);
    Page<Recipe> findAllByName(String name, int pageNumber, int pageSize);
    Page<Recipe> findAllByCategory(Long categoryId, int pageNumber, int pageSize);
    Page<Recipe> findAllByNameAndCategory(String name, Long categoryId, int pageNumber, int pageSize);
    List<Recipe> findAllByRecipeUser(String username);
}
