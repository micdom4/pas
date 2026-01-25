package mvc.src.main.java.team.four.mvc.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import team.four.pas.controllers.DTOs.UserType;

@Controller
public class ViewController {

    private static final String API_URL = "http://localhost:8080/users";
    private final RestTemplate restTemplate;

    public ViewController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/superModernSite")
    public String showRegisterPage(Model model) {
        model.addAttribute("newClient", new ClientDTO());
        model.addAttribute("userTypeValues", UserType.values());

        return "index";
    }

    @PostMapping("/superModernSite")
    public String registerUser(@ModelAttribute ClientAddDTO clientAddDTO, Model model) {
        try {
            restTemplate.postForObject(API_URL, clientAddDTO, ClientDTO.class);

            model.addAttribute("successMessage", "User " + clientAddDTO.getLogin() + " has been registered successfully!");

            model.addAttribute("newClient", new ClientDTO());
            model.addAttribute("userTypeValues", UserType.values());

            return "index";

        } catch (HttpClientErrorException e) {
            model.addAttribute("errorMessage", "Error during registration: " + e.getResponseBodyAsString()); // Lub własny komunikat

            model.addAttribute("newClient", clientAddDTO);
            model.addAttribute("userTypeValues", UserType.values());

            return "index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unknown error: " + e.getMessage());
            model.addAttribute("newClient", clientAddDTO);
            model.addAttribute("userTypeValues", UserType.values());
            return "index";
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ClientDTO {
        private String id;
        private String login;
        private String name;
        private String surname;
        private String type;
        private boolean active;
    }

    @Getter
    @Setter
    private static class ClientAddDTO {
        private String login;
        private String name;
        private String surname;
        private String type;
    }
}