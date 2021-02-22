package mk.napijse.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {

    @GetMapping("/about-us")
    public String getAboutUsPage(Model model){
        model.addAttribute("bodyContent", "about");
        return "master-template";
    }

}
