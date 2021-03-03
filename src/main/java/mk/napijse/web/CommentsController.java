package mk.napijse.web;

import mk.napijse.model.entities.Comment;
import mk.napijse.model.exceptions.CommentNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.service.CommentService;
import mk.napijse.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CommentsController {
    //test
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

    @GetMapping("/edit-comment/{recipeId}/{commentId}")
    public String getEditCommentPage(@PathVariable Long recipeId,
                                     @PathVariable Long commentId,
                                     Model model){
        try{
            Comment comment = this.commentService.findById(commentId);
            model.addAttribute("comment", comment);
            return "edit-comment";
        }catch (CommentNotFoundException exception){
            return "redirect:/recipes/info/"+recipeId + "?error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit-comment/{recipeId}/{commentId}")
    public String editComment(@PathVariable Long recipeId,
                              @PathVariable Long commentId,
                              @RequestParam String content){
        try{
            this.commentService.edit(recipeId, commentId, content);
            return "redirect:/recipes/info/"+recipeId;
        }catch (RecipeNotFoundException | CommentNotFoundException exception){
            return "redirect:/recipes/info/"+recipeId + "?error=" + exception.getMessage();
        }
    }

    @PostMapping("/delete-comment/{recipeId}/{commentId}")
    public String deleteComment(@PathVariable Long recipeId,
                              @PathVariable Long commentId) {
        try {
            this.commentService.delete(recipeId, commentId);
            return "redirect:/recipes/info/" + recipeId;
        } catch (RecipeNotFoundException | CommentNotFoundException exception) {
            return "redirect:/recipes/info/" + recipeId + "?error=" + exception.getMessage();
        }
    }
}
