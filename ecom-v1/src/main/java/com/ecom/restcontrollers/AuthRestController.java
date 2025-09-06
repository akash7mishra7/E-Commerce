package com.ecom.restcontrollers;

import com.ecom.config.CustomUser;
import com.ecom.dtos.JwtResponse;
import com.ecom.dtos.LoginRequest;
import com.ecom.dtos.SignupRequest;
import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        // Use your CustomUser class
        CustomUser principal = (CustomUser) auth.getPrincipal();

        String token = jwtUtil.generateToken(principal.getUsername());
        Set<String> roles = principal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        UserDtls u = userRepository.findByEmail(principal.getUsername());
        Integer uid = u != null ? u.getId() : null;
        String name = u != null ? u.getName() : principal.getUsername();

        JwtResponse resp = new JwtResponse(token, uid, name, roles);
        return ResponseEntity.ok(resp);
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        UserDtls u = new UserDtls();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setMobileNumber(req.getMobile());
        u.setRole("ROLE_USER"); // default role
        u.setIsEnable(true);
        u.setAccountNonLocked(true);

        userRepository.save(u);

        // Generate token after successful registration
        String token = jwtUtil.generateToken(u.getEmail());
        Set<String> roles = Set.of("ROLE_USER");

        JwtResponse resp = new JwtResponse(token, u.getId(), u.getName(), roles);
        return ResponseEntity.status(201).body(resp);
    }
}
