package mk.napijse.web;

import mk.napijse.service.CommentService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                             @RequestParam String content){
        //TODO: get user from session
        this.commentService.save(recipeId, null, content);
        return "redirect:/recipes/info/" + recipeId;
    }
}
