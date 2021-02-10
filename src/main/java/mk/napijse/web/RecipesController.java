package mk.napijse.web;

import mk.napijse.model.Category;
import mk.napijse.model.Recipe;
import mk.napijse.service.CategoryService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RecipesController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    public RecipesController(RecipeService recipeService,
                             CategoryService categoryService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
    }

    @GetMapping("/recipes")
    public String listAllRecipes(@RequestParam(required = false) String error, Model model){
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Recipe> recipes = this.recipeService.findAll();
        model.addAttribute("recipes", recipes);
        return "recipes";
    }

    @PostMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        this.recipeService.deleteById(id);
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/edit/{id}")
    public String getEditRecipePage(@PathVariable Long id,
                                    Model model) {
        if (this.recipeService.findById(id).isPresent()) {
            Recipe recipe = this.recipeService.findById(id).get();
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("recipe", recipe);
            return "add-recipe";
        }
        return "redirect:/recipes?error=RecipeNotFound";
    }

    @GetMapping("/add-new-recipe")
    public String getAddProductPage(Model model) {
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("categories", categories);
        return "add-recipe";
    }

    @PostMapping("/add-new-recipe/{id}")
    public String editRecipe(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String ingredients,
            @RequestParam Long category) {
        this.recipeService.editRecipe(id, name, description, ingredients, category);
        return "redirect:/recipes";
    }

    @PostMapping("/add-new-recipe")
    public String saveNewRecipe(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String ingredients,
            @RequestParam Long category) {

        this.recipeService.saveRecipe(name, description, ingredients, category);
        return "redirect:/recipes";
    }
}
