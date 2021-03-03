package mk.napijse.web;

import mk.napijse.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
class CalendarController {

    private final EventService eventService;

    public CalendarController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping(value="/staticcalendar", method=RequestMethod.GET)
    public ModelAndView staticcalendar() {
        return new ModelAndView("staticcalendar");
    }

    @RequestMapping(value="/calendar", method=RequestMethod.GET)
    public ModelAndView calendar() {
        return new ModelAndView("calendar");
    }

    @RequestMapping(value="/jsoncalendar", method=RequestMethod.GET)
    public ModelAndView jsoncalendar() {
        return new ModelAndView("jsoncalendar");
    }

    @RequestMapping(value="/eventlist", method=RequestMethod.GET)
    public String events(HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        model.addAttribute("events", this.eventService.findAllByUser(username));
        return "events";
    }
}
