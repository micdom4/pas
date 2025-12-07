package team.four.pas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserModDTO;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
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
    private final UserService userService;
    private final UserToDTO userToDTO;

    @GetMapping({""})
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDataList(userService.getAll()));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDTO(userService.findById(id)));
    }

    @GetMapping({"/login/{login}"})
    public ResponseEntity<UserDTO> findPersonByLogin(@PathVariable String login) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDTO(userService.findByLogin(login)));
    }

    @GetMapping({"/search/{login}"})
    public ResponseEntity<List<UserDTO>> searchByLogin(@PathVariable String login) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDataList(userService.findByMatchingLogin(login)));
    }

    @PutMapping({"/{id}/activate"})
    public ResponseEntity<?> activateUser(@PathVariable String id) {
        userService.activate(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping({"/{id}/deactivate"})
    public ResponseEntity<?> deactivateUser(@PathVariable String id) {
        userService.deactivate(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<UserDTO> createUser(@RequestBody UserAddDTO addDTO) {
        User user = userToDTO.toData(addDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userToDTO.toDTO(userService.add(user)));
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> editUser(@PathVariable String id, @RequestBody UserModDTO modDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.update(id, modDTO.surname()));
    }

}