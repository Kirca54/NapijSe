package mk.napijse.web;

import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.InvalidUserCredentialsException;
import mk.napijse.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authentication;

    public LoginController(AuthenticationService authentication) {
        this.authentication = authentication;
    }

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error,
                               Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent","login");
        return "master-template";
    }

    @PostMapping("/post")
    public String login(HttpServletRequest request, Model model) {
        User user = null;
        try{
            user = this.authentication.login(request.getParameter("username"),
                    request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/home";
        }
        catch (InvalidUserCredentialsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());

            model.addAttribute("bodyContent", "login");
            return "master-template";
        }
    }
}
