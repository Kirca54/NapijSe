package mk.napijse.web;

import mk.napijse.model.Category;
import mk.napijse.model.Recipe;
import mk.napijse.model.exceptions.CategoryNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.service.CategoryService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class RecipeManipulationController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    public RecipeManipulationController(RecipeService recipeService,
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
        List<Category> categories = this.categoryService.findAll();

        model.addAttribute("categories", categories);
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
                                    @RequestParam(required = false) String error,
                                    Model model) {
        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
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
    public String getAddRecipePage(Model model) {
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
        try {
            this.recipeService.editRecipe(id, name, description, ingredients, category);
            return "redirect:/recipes";
        } catch (CategoryNotFoundException | RecipeNotFoundException exception){
            return "redirect:/recipes/edit/" + id + "?error=" + exception.getMessage();
        }
    }

    @PostMapping("/add-new-recipe")
    public String saveNewRecipe(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String ingredients,
            @RequestParam Long category,
            HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        this.recipeService.saveRecipe(name, description, ingredients, category, username);
        return "redirect:/recipes";
    }
}