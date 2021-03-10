package mk.napijse.web;

import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.*;
import mk.napijse.service.AuthenticationService;
import mk.napijse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    public RegisterController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent","register");
        return "master-template";
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token,
                             Model model){
        userService.verifyUser(token);
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String email,
                           @RequestParam String name,
                           @RequestParam String surname,
                           Model model) {
        try{
            this.userService.register(username, password, repeatedPassword, email, name, surname, Role.ROLE_USER);
            model.addAttribute("bodyContent", "verification");
            return "master-template";
        } catch (EmailAlreadyExistsException
                | InvalidUsernameOrPasswordException
                | PasswordsDoNotMatchException
                | UsernameAlreadyExistsException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }
}
