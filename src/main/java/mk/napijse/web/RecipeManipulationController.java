package mk.napijse.web;

import mk.napijse.model.entities.Category;
import mk.napijse.model.entities.Recipe;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.CategoryNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.service.CategoryService;
import mk.napijse.service.RecipeService;
import mk.napijse.service.UserService;
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
    private final UserService userService;

    public RecipeManipulationController(RecipeService recipeService,
                                        CategoryService categoryService,
                                        UserService userService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.userService = userService;
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

        model.addAttribute("bodyContent", "recipes");
        return "master-template";
    }

    @PostMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        if (this.recipeService.findById(id).isPresent() &&
                this.recipeService.findById(id).get().getRecipeUser().getUsername().equals(username)){
            this.recipeService.deleteById(id);
            return "redirect:/recipes";
        } else if (this.userService.findByUsername(username).isPresent() &&
                this.userService.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN) &&
                this.recipeService.findById(id).isPresent()){
            this.recipeService.deleteById(id);
            return "redirect:/recipes";
        }
        return "redirect:/recipes?error=You can't delete someone else's recipe, or the recipe doesn't exist";
    }

    @GetMapping("/recipes/edit/{id}")
    public String getEditRecipePage(@PathVariable Long id,
                                    @RequestParam(required = false) String error,
                                    Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        String username = principal.getName();

        if (error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        if ((this.recipeService.findById(id).isPresent() &&
                this.recipeService.findById(id).get().getRecipeUser().getUsername().equals(username)) ||
                (this.userService.findByUsername(username).isPresent() &&
                        this.userService.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN) &&
                        this.recipeService.findById(id).isPresent())) {
            Recipe recipe = this.recipeService.findById(id).get();
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("recipe", recipe);

            model.addAttribute("bodyContent", "add-recipe");
            return "master-template";
        }
        return "redirect:/recipes?error=You can't edit someone else's recipe, or the recipe doesn't exist";
    }

    @GetMapping("/add-new-recipe")
    public String getAddRecipePage(Model model) {
        List<Category> categories = this.categoryService.findAll();

        model.addAttribute("categories", categories);

        model.addAttribute("bodyContent", "add-recipe");
        return "master-template";
    }

    @PostMapping("/add-new-recipe/{id}")
    public String editRecipe(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String ingredients,
            @RequestParam Long category,
            HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            String username = principal.getName();

            if((this.recipeService.findById(id).isPresent() &&
                    this.recipeService.findById(id).get().getRecipeUser().getUsername().equals(username)) ||
                    (this.userService.findByUsername(username).isPresent() &&
                            this.userService.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN) &&
                            this.recipeService.findById(id).isPresent())){
                this.recipeService.editRecipe(id, name, description, ingredients, category);
                return "redirect:/recipes";
            }
            return "redirect:/recipes?error=You can't edit someone else's recipe, or the recipe doesn't exist";
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
