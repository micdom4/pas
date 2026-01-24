package team.four.mvc.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team.four.pas.controllers.DTOs.UserType;

@Controller
public class ViewController {

    @GetMapping("/superModernSite")
    public String index(Model model){
        model.addAttribute("userTypeValues", UserType.values());
        return "index";
    }

}
