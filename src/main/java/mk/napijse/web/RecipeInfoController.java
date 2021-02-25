package mk.napijse.web;

import mk.napijse.model.entities.Comment;
import mk.napijse.model.entities.Recipe;
import mk.napijse.model.enumerations.Role;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.CommentService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RecipeInfoController {

    private final RecipeService recipeService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    public RecipeInfoController(RecipeService recipeService,
                                CommentService commentService,
                                UserRepository userRepository) {
        this.recipeService = recipeService;
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/recipes/info/{id}")
    public String getSelectedRecipePage(@PathVariable Long id,
                                        @RequestParam(required = false) String error,
                                        Model model,
                                        HttpServletRequest request){
        if (request.getUserPrincipal() != null){
            String username = request.getUserPrincipal().getName();
            if (this.userRepository.findByUsername(username).isPresent() &&
                    this.userRepository.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN))
                model.addAttribute("isAdmin", true);
            else model.addAttribute("isAdmin", false);
        }
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        if (this.recipeService.findById(id).isPresent()){
            Recipe recipe = this.recipeService.findById(id).get();
            List<Comment> comments = this.commentService.findAllByRecipe(id);

            model.addAttribute("comments", comments);
            model.addAttribute("recipe", recipe);

            model.addAttribute("bodyContent", "recipeInfo");
            return "recipeInfo";
        }

        return "redirect:/recipes?error=RecipeNotFound";
    }
}
