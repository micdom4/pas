package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserModDTO;
import team.four.pas.exceptions.user.*;
import team.four.pas.services.UserService;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
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
    private final @NonNull UserToDTO userToDTO;

    @GetMapping({""})
    public ResponseEntity<List<UserDTO>> getAll() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userToDTO.toDataList(userService.getAll()));
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userToDTO.toDTO(userService.findById(id)));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (UserIdException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping({"/login/{login}"})
    public ResponseEntity<UserDTO> findPersonByLogin(@PathVariable String login) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userToDTO.toDTO(userService.findByLogin(login)));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (UserLoginException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping({"/search/{login}"})
    public ResponseEntity<List<UserDTO>> searchByLogin(@PathVariable String login) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDataList(userService.findByMatchingLogin(login)));
    }

    @PostMapping({"/{id}/activate"})
    public ResponseEntity<?> activateUser(@PathVariable String id) {
        try {
            userService.activate(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserIdException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping({"/{id}/deactivate"})
    public ResponseEntity<?> deactivateUser(@PathVariable String id) {
        try {
            userService.deactivate(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (UserIdException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<UserDTO> createUser(@RequestBody UserAddDTO addDTO) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userService.add(addDTO));
        } catch (UserTypeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(null);
        } catch (UserDataException | UserLoginException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> editUser(@PathVariable String id, @RequestBody UserModDTO modDTO) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.update(id, modDTO.surname()));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (UserIdException e) {
            return  ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (UserDataException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}