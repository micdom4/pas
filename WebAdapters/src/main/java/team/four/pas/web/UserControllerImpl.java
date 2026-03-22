package team.four.pas.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.four.pas.data.users.User;
import team.four.pas.outside.AuthWebPort;
import team.four.pas.outside.UserWebPort;
import team.four.pas.web.DTO.UserAddDTO;
import team.four.pas.web.DTO.UserDTO;
import team.four.pas.web.DTO.UserModDTO;
import team.four.pas.web.DTO.mappers.UserToDTO;

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
    private final UserWebPort userService;
    private final UserToDTO userToDTO;
    private final AuthWebPort authService;

    @GetMapping({""})
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER)")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDataList(userService.getAll()));
    }

    @GetMapping({"/{id}"})
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER)")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDTO(userService.findById(id)));
    }

    @GetMapping({"/login/{login}"})
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER, " +
            "T(team.four.pas.services.data.UserRoles).CLIENT)")
    public ResponseEntity<UserDTO> findPersonByLogin(@PathVariable
                                                      @NotNull(message = "login can't be null")
                                                      @Pattern(regexp = "^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$",
                                                              message = "Wrong format of login")
                                                      String login) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDTO(userService.findByLogin(login)));
    }

    @GetMapping({"/search/{login}"})
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER)")
    public ResponseEntity<List<UserDTO>> searchByLogin(@PathVariable
                                                               String login) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToDTO.toDataList(userService.findByMatchingLogin(login)));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER) " +
            "&& @ownershipChecker.isValidJws(#id, #jws)")
    public ResponseEntity<Void> activateUser(
            @PathVariable String id,
            @RequestHeader(value = "If-Match") String jws) {
        System.out.println("Git");
        userService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping({"/{id}/deactivate"})
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER) " +
            "&& @ownershipChecker.isValidJws(#id, #jws)")
    public ResponseEntity<?> deactivateUser(@PathVariable String id,
                                            @RequestHeader(value = "If-Match") String jws) {
        userService.deactivate(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(
            value = {""},
            consumes = {"application/json"}
    )
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserAddDTO addDTO) {
        User user = userToDTO.toData(addDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userToDTO.toDTO(userService.add(user)));
    }

    @PutMapping(
            value = {"/{id}"},
            consumes = {"application/json"}
    )
    @PreAuthorize(
            "(hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, T(team.four.pas.services.data.UserRoles).MANAGER, " +
            "T(team.four.pas.services.data.UserRoles).CLIENT) && @ownershipChecker.isOwner(authentication, #id))"
    )
    public ResponseEntity<?> editUser(@PathVariable String id, @Valid @RequestBody UserModDTO modDTO) {
        userService.update(id, modDTO.surname());
        return ResponseEntity.ok().build();
    }


}