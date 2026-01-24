package team.four.mvc.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/superModernSite")
    public String index(){
        return "index";
    }

}
