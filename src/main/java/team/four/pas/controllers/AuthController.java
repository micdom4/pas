package team.four.pas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.four.pas.controllers.DTOs.AuthResponse;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserLoginDTO;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
import team.four.pas.services.AuthService;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/auth"}, produces = {"application/json"})
@RequiredArgsConstructor
public class AuthController {

    private final UserToDTO userToDTO;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserAddDTO request){
        return ResponseEntity.ok(authService.register(userToDTO.toData(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDTO request){
        return ResponseEntity.ok(authService.login(request.login(), request.password()));
    }
}
