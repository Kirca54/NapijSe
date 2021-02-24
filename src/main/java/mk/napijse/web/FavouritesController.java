package mk.napijse.web;

import mk.napijse.model.entities.Recipe;
import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.RecipeAlreadyInFavouritesException;
import mk.napijse.model.exceptions.RecipeNotFoundException;
import mk.napijse.service.RecipeService;
import mk.napijse.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FavouritesController {

    private final RecipeService recipeService;
    private final UserService userService;

    public FavouritesController(RecipeService recipeService,
                                UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/{username}/favourites")
    public String getFavouritesPage(@PathVariable String username,
                                    HttpServletRequest request,
                                    Model model){
        System.out.println("In getFavouritesPage");
        try {
            if (request.getUserPrincipal().getName().equals(username)){
                List<Recipe> favourites = this.recipeService.findAllFavourites(username);

                model.addAttribute("username", username);
                model.addAttribute("favourites", favourites);
            }
            else return "redirect:/recipes?error=You can't display other user's favourite recipes";
        }catch (UsernameNotFoundException exception){
            return "redirect:/recipes?error="+exception.getMessage();
        }
        return "favourites";
    }

    @PostMapping("/{username}/delete-from-favourites/{recipeId}")
    public String deleteFromFavourites(@PathVariable String username,
                                       @PathVariable Long recipeId,
                                       HttpServletRequest request){
        System.out.println("In deleteFromFavourites");
        try {
            if (request.getUserPrincipal().getName().equals(username)){
                this.recipeService.deleteFromFavourites(username, recipeId);
                String user = request.getUserPrincipal().getName();
                return "redirect:/"+user+"/favourites";
            }
            else return "redirect:/recipes?error=You can't delete a recipe from other user's favourites";
        }catch (UsernameNotFoundException | RecipeNotFoundException exception){
            return "redirect:/recipes?error="+exception.getMessage();
        }
    }

    @PostMapping("/{username}/add-to-favourites/{recipeId}")
    public String addToFavourites(@PathVariable String username,
                                  @PathVariable Long recipeId,
                                  HttpServletRequest request){
        try {
            if (request.getUserPrincipal().getName().equals(username)){
                User user = (User) this.userService.loadUserByUsername(username);
                if(user.getFavourites()
                        .stream().filter(i -> i.getId().equals(recipeId)).count() > 0)
                    throw new RecipeAlreadyInFavouritesException();
                this.recipeService.addToFavourites(username, recipeId);
                return "redirect:/recipes";
            }
            else return "redirect:/recipes?error=You can't add a recipe in other user's favourites";
        }catch (RecipeAlreadyInFavouritesException |
                UsernameNotFoundException |
                RecipeNotFoundException exception){
            return "redirect:/recipes?error="+exception.getMessage();
        }
    }
}
