package team.four.pas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.*;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
import team.four.pas.security.TokenBlackList;
import team.four.pas.services.AuthService;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/auth"}, produces = {"application/json"})
@RequiredArgsConstructor
public class AuthController {

    private final UserToDTO userToDTO;
    private final AuthService authService;
    private final TokenBlackList tokenBlackList;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserAddDTO request){
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(authService.register(userToDTO.toData(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDTO request){
        return ResponseEntity.ok(authService.login(request.login(), request.password()));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
                  "T(team.four.pas.security.SecurityRoles).MANAGER, " +
                  "T(team.four.pas.security.SecurityRoles).CLIENT)")
    public ResponseEntity<Void> logout(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getCredentials() != null) {
            String jwt = auth.getCredentials().toString();
            tokenBlackList.add(jwt);
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/reset")
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
                  "T(team.four.pas.security.SecurityRoles).MANAGER, " +
                  "T(team.four.pas.security.SecurityRoles).CLIENT)")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO.oldPassword(), changePasswordDTO.newPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/change/{clientId}")
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
                  "T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<Void> prepareIntToken(@PathVariable String clientId) {
        String jws = authService.generateIntegrityToken(clientId);

        return ResponseEntity.ok()
                             .header("X-Data-Integrity", jws)
                             .header("Access-Control-Expose-Headers", "X-Data-Integrity")
                             .build();
    }

    @GetMapping("/change/{clientId}/{vmId}")
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, " +
                  "T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<Void> prepareIntToken(@PathVariable String clientId, @PathVariable String vmId) {
        String jws = authService.generateIntegrityToken(clientId, vmId);

        return ResponseEntity.ok()
                             .header("X-Data-Integrity", jws)
                             .header("Access-Control-Expose-Headers", "X-Data-Integrity")
                             .build();
    }
}
