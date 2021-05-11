package mk.napijse.web;

import mk.napijse.model.entities.Category;
import mk.napijse.model.entities.Recipe;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.CategoryNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.service.CategoryService;
import mk.napijse.service.RecipeService;
import mk.napijse.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String listAllRecipes(@RequestParam(required = false) String error,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size,
                                 @RequestParam(required = false) String recipeName,
                                 @RequestParam(required = false) Long categoryId,
                                 Model model){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page<Recipe> recipePage;
        int totalPages;

        List<Category> categories = this.categoryService.findAll();

        try {
            if (recipeName == null && categoryId == null) {
                recipePage = this.recipeService.findAllPaginated(currentPage, pageSize);
                totalPages = recipePage.getTotalPages();
            } else if (recipeName != null && categoryId == null) {
                recipePage = this.recipeService.findAllByName(recipeName, currentPage, pageSize);
                totalPages = recipePage.getTotalPages();
            } else if (recipeName == null && categoryId != null) {
                recipePage = this.recipeService.findAllByCategory(categoryId, currentPage, pageSize);
                totalPages = recipePage.getTotalPages();
            } else {
                recipePage = this.recipeService.findAllByNameAndCategory(recipeName, categoryId, currentPage, pageSize);
                totalPages = recipePage.getTotalPages();
            }
        } catch (CategoryNotFoundException exception) {
            return "redirect:/recipes?error=" + exception.getMessage();
        }

        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("recipeName", recipeName);

        model.addAttribute("categories", categories);
        model.addAttribute("recipePage", recipePage);

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
