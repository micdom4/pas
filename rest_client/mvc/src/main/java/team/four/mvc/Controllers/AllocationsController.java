package mvc.src.main.java.team.four.mvc.Controllers;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.controllers.DTOs.UserDTO;

@Controller
@RequestMapping("/allocations")
public class AllocationController {

    private static final String API_URL = "http://localhost:8080/api";
    private final RestTemplate restTemplate;

    public AllocationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showAllocationsPage(Model model) {
        UserDTO[] clients = restTemplate.getForObject(API_URL + "/clients", UserDTO[].class);
        ResourceDTO[] resources = restTemplate.getForObject(API_URL + "/resources", ResourceDTO[].class);

        AllocationDTO[] allocations = restTemplate.getForObject(API_URL + "/allocations", AllocationDTO[].class);

        model.addAttribute("clients", clients);
        model.addAttribute("resources", resources);
        model.addAttribute("allocations", allocations);

        model.addAttribute("newAllocation", new AllocationDTO("ss"));

        return "allocation-view";
    }

    @PostMapping("/create")
    public String createAllocation(@ModelAttribute AllocationDTO allocationDto) {
        restTemplate.postForObject(API_URL + "/allocations", allocationDto, AllocationDTO.class);

        return "redirect:/allocations";
    }

    // 3. Obsługa przycisku "Zakończ alokację"
    @PostMapping("/finish/{id}")
    public String finishAllocation(@PathVariable Long id) {
        restTemplate.postForLocation(API_URL + "/allocations/" + id + "/finish", null);

        return "redirect:/allocations";
    }
}

record AllocationDTO(
        String _id
) {
}