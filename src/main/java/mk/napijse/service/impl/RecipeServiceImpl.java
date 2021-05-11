package mk.napijse.service.impl;

import mk.napijse.model.entities.Category;
import mk.napijse.model.entities.Recipe;
import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.CategoryNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.repository.CategoryRepository;
import mk.napijse.repository.RecipeRepository;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Recipe> findAll() {
        return this.recipeRepository.findAll();
    }

    @Override
    public Page<Recipe> findAllPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return this.recipeRepository.findAll(pageable);
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
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        Recipe oldRecipe = this.findById(id).orElseThrow(RecipeNotFoundException::new);
        oldRecipe.setName(name);
        oldRecipe.setDescription(description);
        oldRecipe.setIngredients(ingredients);
        oldRecipe.setCategory(category);
        return this.recipeRepository.save(oldRecipe);
    }

    @Override
    public Recipe saveRecipe(String name, String description, String ingredients, Long categoryId, String username) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        User currentUser = this.userRepository.findByUsername(username).get();
        Recipe recipe = new Recipe(name, description, ingredients, currentUser, category);
        return this.recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> findAllFavourites(String username) {
        User user = this.userRepository
                .findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

        return this.userRepository.findByUsername(username).get().getFavourites();
    }

    @Override
    public Recipe addToFavourites(String username, Long recipeId) {
        User user = this.userRepository
                .findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(username));
        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        user.addToFavourites(recipe);
        this.userRepository.save(user);
        return recipe;
    }

    @Override
    public void deleteFromFavourites(String username, Long recipeId) {
        User user = this.userRepository
                .findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(username));
        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
        user.deleteFromFavourites(recipe);
        this.userRepository.save(user);
    }

    @Override
    public Page<Recipe> findAllByName(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return this.recipeRepository.findAllByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Recipe> findAllByCategory(Long categoryId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        return this.recipeRepository.findAllByCategory(category, pageable);
    }

    @Override
    public Page<Recipe> findAllByNameAndCategory(String name, Long categoryId,  int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        return this.recipeRepository.findAllByNameContainingIgnoreCaseAndCategory(name, category, pageable);
    }

    @Override
    public List<Recipe> findAllByRecipeUser(String username) {
        User user = this.userRepository.findByUsername(username).get();
        return this.recipeRepository.findAllByRecipeUser(user);
    }
}
