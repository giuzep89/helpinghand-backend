package com.giuzep89.helpinghandbackend.controllers;

import com.giuzep89.helpinghandbackend.dtos.AuthInputDTO;
import com.giuzep89.helpinghandbackend.dtos.RegisterInputDTO;
import com.giuzep89.helpinghandbackend.dtos.UserOutputDTO;
import com.giuzep89.helpinghandbackend.mappers.UserMapper;
import com.giuzep89.helpinghandbackend.models.Authority;
import com.giuzep89.helpinghandbackend.models.User;
import com.giuzep89.helpinghandbackend.repositories.AuthorityRepository;
import com.giuzep89.helpinghandbackend.repositories.UserRepository;
import com.giuzep89.helpinghandbackend.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository,
                          AuthorityRepository authorityRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthInputDTO authInputDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authInputDTO.getUsername(),
                            authInputDTO.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(token);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterInputDTO registerInputDTO) {
        if (userRepository.findByUsername(registerInputDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User user = new User();
        user.setEmail(registerInputDTO.getEmail());
        user.setUsername(registerInputDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerInputDTO.getPassword()));
        user.setEnabled(true);

        Authority userRole = authorityRepository.findById("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(new Authority("ROLE_USER")));

        Set<Authority> authorities = new HashSet<>();
        authorities.add(userRole);
        user.setAuthorities(authorities);

        User savedUser = userRepository.save(user);
        UserOutputDTO outputDTO = UserMapper.toDTO(savedUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(savedUser.getUsername())
                .toUri();

        return ResponseEntity.created(location).body(outputDTO);
    }
}
