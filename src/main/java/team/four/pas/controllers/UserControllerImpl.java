package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.User;

import java.util.List;

@RestController
@CrossOrigin(
        originPatterns = {"http://localhost:[*]"}
)
@RequestMapping(
        value = {"/users"},
        produces = {"application/json"}
)
@RequiredArgsConstructor
public class UserControllerImpl {
    private final @NonNull UserService userService;

    @GetMapping({""})
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping({"/{id}"})
    public User getUser(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping({"/login/{login}"})
    public User findPersonByLogin(@PathVariable String login) {
        return userService.findByLogin(login);
    }

    @PostMapping({"/{id}/activate"})
    public void activateUser(@PathVariable String id) {
        userService.activate(id);
    }

    @PostMapping({"/{id}/deactivate"})
    public void deactivatePerson(@PathVariable String id) {
        userService.deactivate(id);
    }


    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public UserDTO createPerson(@RequestBody UserAddDTO addDTO) {
        return userService.add(addDTO);
    }


    /*
    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public User editPerson(@PathVariable String id, @RequestBody String surname) {
        return userService.updatePerson(id, surname);
    }
     */

}