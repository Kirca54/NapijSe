package mk.napijse.web;

import mk.napijse.model.exceptions.InvalidTokenException;
import mk.napijse.model.exceptions.PasswordsDoNotMatchException;
import mk.napijse.model.exceptions.UserNotFoundException;
import mk.napijse.service.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ResetPasswordController {

    private final AuthenticationService authenticationService;

    public ResetPasswordController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(@RequestParam(required = false) String error,
                                        final Model model){
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent","forgot-password");
        return "master-template";
    }

    @PostMapping("/password/request")
    public String sendResetPasswordEmail(@RequestParam String username,
                                         Model model){
        try{
            this.authenticationService.forgotPassword(username);
        }catch (UserNotFoundException exception){
            return "redirect:/forgot-password?error="+exception.getMessage();
        }
        model.addAttribute("bodyContent", "reset-password");
        return "master-template";
    }

    @GetMapping("/password/change")
    public String getChangePasswordPage(@RequestParam(required = false) String token,
                                        final Model model,
                                        HttpServletRequest request){
        if (StringUtils.isEmpty(token)) {
            return "redirect:/login?error=Token error";
        }

        request.getSession().setAttribute("token", token);
        model.addAttribute("token", token);
        model.addAttribute("bodyContent", "change-password");
        return "master-template";
    }

    @PostMapping("/password/change")
    public String resetPassword(@RequestParam String password,
                                @RequestParam String repeatPassword,
                                HttpServletRequest request){
        String token = (String) request.getSession().getAttribute("token");
        try {
            this.authenticationService.updatePassword(password, repeatPassword, token);
        } catch (PasswordsDoNotMatchException | InvalidTokenException | UserNotFoundException exception) {
            return "redirect:/password/change?error="+exception.getMessage();
        }
        return "redirect:/login?error=Password changed successfully";
    }
}
