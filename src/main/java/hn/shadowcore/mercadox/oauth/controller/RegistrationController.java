package hn.shadowcore.mercadox.oauth.controller;


import hn.shadowcore.mercadox.oauth.service.AuthService;
import hn.shadowcore.mercadox.oauth.service.RegistrationService;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import hn.shadowcore.mercadox.library.entity.request.RegisterRequestDto;
import hn.shadowcore.mercadox.library.entity.response.BaseResponseDto;
import hn.shadowcore.mercadox.library.entity.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/public/orgs")
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthService authService;

    private final RegistrationService registrationService;

    @PostMapping("/{orgId}/register")
    public ResponseEntity<? extends Response<User>> signUp
            (@PathVariable(value = "orgId") final String orgId,
             @RequestBody @Valid RegisterRequestDto registerRequestDto) {

        BaseResponseDto<User> baseResponse = new BaseResponseDto<>();
        final User createdUser = authService.createUser(registerRequestDto);
        registrationService.registerUser(createdUser);
        return baseResponse.buildResponseEntity(HttpStatus.CREATED, "User created successfully.", createdUser);

    }

}
