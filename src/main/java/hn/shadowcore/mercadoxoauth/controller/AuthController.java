package hn.shadowcore.mercadoxoauth.controller;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.UserDetailsImpl;
import hn.shadowcore.mercadoxlibrary.entity.request.AuthRequestDto;
import hn.shadowcore.mercadoxlibrary.entity.request.RegisterRequestDto;
import hn.shadowcore.mercadoxlibrary.entity.response.BaseResponseDto;
import hn.shadowcore.mercadoxlibrary.entity.response.Response;
import hn.shadowcore.mercadoxoauth.service.AuthService;
import hn.shadowcore.mercadoxoauth.service.CustomUserDetailsService;
import hn.shadowcore.mercadoxoauth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtils;

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<? extends Response<String>> login(@RequestBody @Valid AuthRequestDto authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(userDetails.getUser()); // Email acts as username.

        BaseResponseDto<String> jwtResponse = new BaseResponseDto<>();

        return jwtResponse.buildResponseEntity(HttpStatus.OK, "User authenticated successfully", jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<? extends Response<User>> signUp(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        BaseResponseDto<User> baseResponse = new BaseResponseDto<>();
        //TODO: Send email verification.
        final User createdUser = authService.createUser(registerRequestDto);
        return baseResponse.buildResponseEntity(HttpStatus.CREATED, "User created successfully.", createdUser);
    }

}
