package mk.napijse.service;

import mk.napijse.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAll();
    void deleteById(Long id);
    Optional<Recipe> findById(Long id);
    Recipe editRecipe(Long id, String name, String description, String ingredients, Long category);
    Recipe saveRecipe (String name, String description, String ingredients, Long category);
}
