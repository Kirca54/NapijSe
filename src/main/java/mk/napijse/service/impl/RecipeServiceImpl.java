package mk.napijse.service.impl;

import mk.napijse.model.Category;
import mk.napijse.model.Recipe;
import mk.napijse.model.exceptions.CategoryNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.repository.CategoryRepository;
import mk.napijse.repository.RecipeRepository;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Recipe> findAll() {
        return this.recipeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.recipeRepository.deleteById(id);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return this.recipeRepository.findById(id);
    }

    @Override
    public Recipe editRecipe(Long id, String name, String description, String ingredients, Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId).get();

        Recipe oldRecipe = this.findById(id).orElseThrow(RecipeNotFoundException::new);
        oldRecipe.setName(name);
        oldRecipe.setDescription(description);
        oldRecipe.setIngredients(ingredients);
        oldRecipe.setCategory(category);
        return this.recipeRepository.save(oldRecipe);
    }

    @Override
    public Recipe saveRecipe(String name, String description, String ingredients, Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        Recipe recipe = new Recipe(name, description, ingredients, null, category);
        return this.recipeRepository.save(recipe);
    }
}
