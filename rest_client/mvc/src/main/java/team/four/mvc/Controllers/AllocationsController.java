package mvc.src.main.java.team.four.mvc.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import team.four.pas.controllers.DTOs.AllocationAddDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Controller
@RequestMapping("/allocations")
public class AllocationsController {

    private static final String API_URL = "http://localhost:8080";
    private final RestTemplate restTemplate;

    public AllocationsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showAllocationsPage(Model model) {
        fetchData(model);

        model.addAttribute("newAllocation", new AllocationDTO());

        return "allocations-view";
    }

    @PostMapping("/create")
    public String createAllocation(@ModelAttribute AllocationDTO allocationDto, Model model) {
        try {
            AllocationAddDTO payload = new AllocationAddDTO(allocationDto.client.id, allocationDto.vm.id);

            restTemplate.postForObject(API_URL + "/allocations", payload, AllocationDTO.class);

            return "redirect:/allocations";
        } catch (HttpClientErrorException e) {

            String message = switch (e.getStatusCode()) {
                case HttpStatus.CONFLICT -> "Selected Resource is already allocated!";
                case HttpStatus.FORBIDDEN -> "Selected Client is not active and cannot perform an Allocation!";
                case HttpStatus.NOT_FOUND -> "No such Client or Resource.";
                default -> e.getMessage();
            };

            model.addAttribute("errorMessage", message);
            model.addAttribute("newAllocation", allocationDto);
            fetchData(model);

            return "allocations-view";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unknown error: " + e.getMessage());
            model.addAttribute("newAllocation", allocationDto);
            fetchData(model);
            return "allocations-view";
        }
    }

    @PostMapping("/finish/{id}")
    public String finishAllocation(@PathVariable String id) {
        restTemplate.put(API_URL + "/allocations/" + id + "/finish", null);

        return "redirect:/allocations";
    }

    private void fetchData(Model model) {
        UserDTO[] clients = restTemplate.getForObject(API_URL + "/users", UserDTO[].class);
        ResourceDTO[] resources = restTemplate.getForObject(API_URL + "/resources", ResourceDTO[].class);

        AllocationDTO[] allocations = restTemplate.getForObject(API_URL + "/allocations", AllocationDTO[].class);

        assert clients != null;
        model.addAttribute("clients", Arrays.stream(clients).filter(
                c -> c.active && Objects.equals(c.type, "CLIENT")).toArray());
        model.addAttribute("resources", resources);
        model.addAttribute("allocations", allocations);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AllocationDTO {
        private String id;
        private UserDTO client;
        private ResourceDTO vm;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserDTO {
        private String id;
        private String login;
        private String password;
        private String name;
        private String surname;
        private String type;
        private boolean active;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ResourceDTO {
        private String id;
        private int cpuNumber;
        private int ramGiB;
        private int storageGiB;
    }
}