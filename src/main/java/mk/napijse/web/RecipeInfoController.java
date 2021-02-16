package mk.napijse.web;

import mk.napijse.model.Comment;
import mk.napijse.model.Recipe;
import mk.napijse.service.CommentService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class RecipeInfoController {

    private final RecipeService recipeService;
    private final CommentService commentService;

    public RecipeInfoController(RecipeService recipeService,
                                CommentService commentService) {
        this.recipeService = recipeService;
        this.commentService = commentService;
    }

    @GetMapping("/recipes/info/{id}")
    public String getSelectedRecipePage(@PathVariable Long id,
                                        Model model){
        if (this.recipeService.findById(id).isPresent()){
            Recipe recipe = this.recipeService.findById(id).get();
            List<Comment> comments = this.commentService.findAllByRecipe(id);

            model.addAttribute("comments", comments);
            model.addAttribute("recipe", recipe);
            return "recipeInfo";
        }

        return "redirect:/recipes?error=RecipeNotFound";
    }
}
