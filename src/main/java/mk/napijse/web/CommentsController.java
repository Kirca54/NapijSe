package mk.napijse.web;

import mk.napijse.service.CommentService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CommentsController {

    private final CommentService commentService;
    private final RecipeService recipeService;

    public CommentsController(CommentService commentService, RecipeService recipeService) {
        this.commentService = commentService;
        this.recipeService = recipeService;
    }

    @PostMapping("/add-comment/{recipeId}")
    public String addComment(@PathVariable Long recipeId,
                             @RequestParam String content,
                             HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        this.commentService.save(recipeId, username, content);
        return "redirect:/recipes/info/" + recipeId;
    }
}
