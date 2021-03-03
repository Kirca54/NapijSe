package mk.napijse.web;

import mk.napijse.model.entities.Category;
import mk.napijse.model.entities.Recipe;
import mk.napijse.model.exceptions.CategoryNotFoundException;
import mk.napijse.service.CategoryService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchRecipesController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    public SearchRecipesController(RecipeService recipeService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
    }

    @PostMapping("/recipes/search")
    public String searchRecipes(@RequestParam(required = false) String recipeName,
                                @RequestParam(required = false) Long categoryId,
                                Model model){
        try {
            if (recipeName==null && categoryId==null){
                return "redirect:/recipes";
            }
            else if (recipeName!=null && categoryId==null){
                List<Recipe> recipes = this.recipeService.findAllByName(recipeName);
                List<Category> categories = this.categoryService.findAll();

                model.addAttribute("categories", categories);
                model.addAttribute("recipes", recipes);
                return "recipes";
            }
            else if (recipeName==null && categoryId!=null){
                List<Recipe> recipes = this.recipeService.findAllByCategory(categoryId);
                List<Category> categories = this.categoryService.findAll();

                model.addAttribute("categories", categories);
                model.addAttribute("recipes", recipes);
                return "recipes";
            }
            else {
                List<Recipe> recipes = this.recipeService.findAllByNameAndCategory(recipeName, categoryId);
                List<Category> categories = this.categoryService.findAll();

                model.addAttribute("categories", categories);
                model.addAttribute("recipes", recipes);
                return "recipes";
            }
        }catch (CategoryNotFoundException exception){
            return "redirect:/recipes?error="+exception.getMessage();
        }
    }
}
