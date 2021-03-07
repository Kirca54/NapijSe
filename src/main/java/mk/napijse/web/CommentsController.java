package mk.napijse.web;

import mk.napijse.model.entities.Comment;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.CommentNotFoundException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.service.CommentService;
import mk.napijse.service.RecipeService;
import mk.napijse.service.UserService;
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

    private final CommentService commentService;
    private final RecipeService recipeService;
    private final UserService userService;

    public CommentsController(CommentService commentService,
                              RecipeService recipeService,
                              UserService userService) {
        this.commentService = commentService;
        this.recipeService = recipeService;
        this.userService = userService;
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
                                     Model model,
                                     HttpServletRequest request){

        String username = request.getUserPrincipal().getName();

        try{
            if((this.commentService.findById(commentId).getCommentUser().getUsername().equals(username)) ||
                    (this.userService.findByUsername(username).isPresent() &&
                            this.userService.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN))){
                Comment comment = this.commentService.findById(commentId);
                model.addAttribute("comment", comment);

                model.addAttribute("bodyContent", "edit-comment");
                return "master-template";
            }
            return "redirect:/recipes/info/"+recipeId + "?error=You can't edit other user's comment";
        }catch (CommentNotFoundException exception){
            return "redirect:/recipes/info/"+recipeId + "?error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit-comment/{recipeId}/{commentId}")
    public String editComment(@PathVariable Long recipeId,
                              @PathVariable Long commentId,
                              @RequestParam String content,
                              HttpServletRequest request){

        String username = request.getUserPrincipal().getName();

        try{
            if ((this.commentService.findById(commentId).getCommentUser().getUsername().equals(username)) ||
                    (this.userService.findByUsername(username).isPresent() &&
                            this.userService.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN))){
                this.commentService.edit(recipeId, commentId, content);
                return "redirect:/recipes/info/"+recipeId;
            }
            return "redirect:/recipes/info/"+recipeId + "?error=You can't edit other user's comment";
        }catch (RecipeNotFoundException | CommentNotFoundException exception){
            return "redirect:/recipes/info/"+recipeId + "?error=" + exception.getMessage();
        }
    }

    @PostMapping("/delete-comment/{recipeId}/{commentId}")
    public String deleteComment(@PathVariable Long recipeId,
                              @PathVariable Long commentId,
                                HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();

        try {
            if ((this.commentService.findById(commentId).getCommentUser().getUsername().equals(username)) ||
                    (this.userService.findByUsername(username).isPresent() &&
                            this.userService.findByUsername(username).get().getRole().equals(Role.ROLE_ADMIN))){
                this.commentService.delete(recipeId, commentId);
                return "redirect:/recipes/info/" + recipeId;
            }
            return "redirect:/recipes/info/"+recipeId + "?error=You can't delete other user's comment";
        } catch (RecipeNotFoundException | CommentNotFoundException exception) {
            return "redirect:/recipes/info/" + recipeId + "?error=" + exception.getMessage();
        }
    }
}
