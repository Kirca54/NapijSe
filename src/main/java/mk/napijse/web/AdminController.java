package mk.napijse.web;

import mk.napijse.model.entities.User;
import mk.napijse.model.enumerations.Role;
import mk.napijse.model.exceptions.UserNotFoundException;
import mk.napijse.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin-page")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminPage(@RequestParam(required = false) String error,
                               Model model){
        if (error != null){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<User> users = this.userService.findAllRegularUsers();
        List<User> admins = this.userService.findAllAdminUsers();

        model.addAttribute("bodyContent", "admin");
        model.addAttribute("users", users);
        model.addAttribute("admins", admins);
        return "admin";
    }

    @PostMapping("/make-admin/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String makeAdmin(@PathVariable String username){
        try {
            this.userService.changeRole(username, Role.ROLE_ADMIN);
            return "redirect:/admin-page";
        }catch (UserNotFoundException exception){
            return "redirect:/admin-page?error="+exception.getMessage();
        }
    }

    @PostMapping("/remove-admin/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeAdmin(@PathVariable String username){
        try {
            this.userService.changeRole(username, Role.ROLE_USER);
            return "redirect:/admin-page";
        }catch (UserNotFoundException exception){
            return "redirect:/admin-page?error="+exception.getMessage();
        }
    }
}
