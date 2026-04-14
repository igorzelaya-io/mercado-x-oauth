package hn.shadowcore.mercadox.oauth.controller;

import hn.shadowcore.mercadox.context.utils.JwtUtil;
import hn.shadowcore.mercadox.library.entity.model.auth.UserDetailsImpl;
import hn.shadowcore.mercadox.library.entity.ports.incoming.RegistrationUseCase;
import hn.shadowcore.mercadox.library.entity.request.AuthRequestDto;
import hn.shadowcore.mercadox.library.entity.response.BaseResponseDto;
import hn.shadowcore.mercadox.library.entity.response.Response;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtils;

    private final RegistrationUseCase registrationService;

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

    @PostMapping("/validate")
    public ResponseEntity<? extends Response<Void>> validate(@RequestParam final String token) {

        BaseResponseDto<Void> baseResponse = new BaseResponseDto<>();
        registrationService.validateUser(token);
        return baseResponse.buildResponseEntity(HttpStatus.OK, "Email was validated successfully.", null);

    }

}
