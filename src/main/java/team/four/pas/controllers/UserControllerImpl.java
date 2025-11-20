package team.four.pas.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.user.*;
import team.four.pas.services.UserService;

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
    public ResponseEntity<List<UserDTO>> getAll() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.getAll());
        } catch (UserGetAllException ex) {
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
                    .body(userService.findById(id));
        } catch (UserFindException e) {
            if (e.getCause() instanceof UserNotPresentException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else if (e.getCause() instanceof UserInvalidIdException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @GetMapping({"/login/{login}"})
    public ResponseEntity<UserDTO> findPersonByLogin(@PathVariable String login) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.findByLogin(login));
        } catch (UserFindException e) {
            if (e.getCause() instanceof UserNotPresentException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else if (e.getCause() instanceof UserInvalidLoginException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @GetMapping({"/search/{login}"})
    public ResponseEntity<List<UserDTO>> searchByLogin(@PathVariable String login) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.findByMatchingLogin(login));
        } catch (UserFindException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping({"/{id}/activate"})
    public ResponseEntity<?> activateUser(@PathVariable String id) {
        try {
            userService.activate(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } catch (UserUpdateException ex) {
            if (ex.getCause() instanceof UserNotPresentException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else if (ex.getCause() instanceof UserInvalidIdException) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @PostMapping({"/{id}/deactivate"})
    public ResponseEntity<?> deactivatePerson(@PathVariable String id) {
        try {
            userService.deactivate(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } catch (UserUpdateException e) {
            if (e.getCause() instanceof UserNotPresentException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else if (e.getCause() instanceof UserInvalidIdException) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<UserDTO> createPerson(@RequestBody UserAddDTO addDTO) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userService.add(addDTO));
        } catch (UserAddException ue) {
            if (ue.getCause() instanceof UserDataValidationException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            } else if (ue.getCause() instanceof UserAlreadyExistsException) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    public ResponseEntity<?> editPerson(@PathVariable String id, @RequestBody String surname) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.update(id, surname));
        } catch (UserUpdateException e) {
            if (e.getCause() instanceof UserNotPresentException) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else if (e.getCause() instanceof UserInvalidIdException) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(null);
            } else if (e.getCause() instanceof UserDataValidationException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }
    }

}