package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.UserService;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
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
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @GetMapping({"/{id}"})
    public UserDTO getUser(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping({"/login/{login}"})
    public UserDTO findPersonByLogin(@PathVariable String login) {
        return userService.findByLogin(login);
    }

    @GetMapping({"/search/{login}"})
    public List<UserDTO> searchByLogin(@PathVariable String login) {
        return userService.findByMatchingLogin(login);
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
    public ResponseEntity<UserDTO> createPerson(@RequestBody UserAddDTO addDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(addDTO));
        } catch (ServerException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (KeyManagementException e) {
            return  ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(null);
        } catch (BadAttributeValueExpException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public UserDTO editPerson(@PathVariable String id, @RequestBody String surname) {
        return userService.update(id, surname);
    }

}