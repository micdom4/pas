package team.four.pas.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.four.pas.outside.AuthWebPort;
import team.four.pas.data.AuthResponse;
import team.four.pas.web.DTO.ChangePasswordDTO;
import team.four.pas.web.DTO.UserAddDTO;
import team.four.pas.web.DTO.UserLoginDTO;
import team.four.pas.web.DTO.mappers.UserToDTO;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/auth"}, produces = {"application/json"})
@RequiredArgsConstructor
public class AuthController {

    private final UserToDTO userToDTO;
    private final AuthWebPort authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserAddDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(userToDTO.toData(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDTO request) {
        return ResponseEntity.ok(authService.login(request.login(), request.password()));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER, " +
            "T(team.four.pas.services.data.UserRoles).CLIENT)")
    public ResponseEntity<Void> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getCredentials() != null) {
            String jwt = auth.getCredentials().toString();
            authService.blacklist(jwt);
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/reset")
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER, " +
            "T(team.four.pas.services.data.UserRoles).CLIENT)")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO.oldPassword(), changePasswordDTO.newPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/change/{clientId}")
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER)")
    public ResponseEntity<Void> prepareIntToken(@PathVariable String clientId) {
        String jws = authService.generateIntegrityToken(clientId);

        return ResponseEntity.ok()
                .header("ETag", jws)
                .build();
    }

    @GetMapping("/change/{clientId}/{vmId}")
    @PreAuthorize("hasAnyRole(T(team.four.pas.services.data.UserRoles).ADMIN, " +
            "T(team.four.pas.services.data.UserRoles).MANAGER)")
    public ResponseEntity<Void> prepareIntToken(@PathVariable String clientId, @PathVariable String vmId) {
        String jws = authService.generateIntegrityToken(clientId, vmId);

        return ResponseEntity.ok()
                .header("ETag", jws)
                .build();
    }
}
