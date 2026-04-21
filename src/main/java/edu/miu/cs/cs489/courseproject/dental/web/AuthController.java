package edu.miu.cs.cs489.courseproject.dental.web;

import edu.miu.cs.cs489.courseproject.dental.dto.auth.AuthResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.auth.CurrentUserResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.auth.LoginRequest;
import edu.miu.cs.cs489.courseproject.dental.security.ClinicUserDetails;
import edu.miu.cs.cs489.courseproject.dental.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        ClinicUserDetails user = (ClinicUserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalize(request.username()), request.password())
        ).getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                user.getUsername(),
                user.getFullName(),
                user.getRoleName()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> currentUser(@AuthenticationPrincipal ClinicUserDetails user) {
        return ResponseEntity.ok(new CurrentUserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRoleName()
        ));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
