package hn.shadowcore.mercadox.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.UserType;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.UserTypeName;
import hn.shadowcore.mercadoxlibrary.entity.request.AuthRequestDto;
import hn.shadowcore.mercadoxlibrary.entity.request.RegisterRequestDto;
import hn.shadowcore.mercadoxlibrary.jpa.config.H2JpaTestConfig;
import hn.shadowcore.mercadoxlibrary.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({ H2JpaTestConfig.class, UserRepository.class })
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("password"))
                .enabled(true)
                .build();

        userRepository.save(user);
    }

    @Test
    void checkSchema() {
        Integer schemas = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.schemata where schema_name in ('AUTH','auth')",
                Integer.class
        );
        System.out.println("schemas=" + schemas);
    }

    // -------------------------
    // LOGIN – SUCCESS
    // -------------------------
    @Test
    void shouldAuthenticateAndReturnJwt() throws Exception {
        AuthRequestDto request = new
                AuthRequestDto("test@test.com", "password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("User authenticated successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    // -------------------------
    // LOGIN – INVALID PASSWORD
    // -------------------------
    @Test
    void shouldRejectInvalidCredentials() throws Exception {
        AuthRequestDto request = new AuthRequestDto("test@test.com", "wrong-password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

//     -------------------------
//     REGISTER – SUCCESS
//     -------------------------
    @Test
    void shouldRegisterUser() throws Exception {

        RegisterRequestDto request = new RegisterRequestDto
                ("mock-username", "password",
                        "m", "z", "test@test.com", UserType
                        .builder()
                                .name(UserTypeName.SUPER_ADMIN)
                                .description("example")
                                .build(),
                        "org-123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("User created successfully."))
                .andExpect(jsonPath("$.data.email")
                        .value("new@test.com"));
    }

//     -------------------------
//     VALIDATE – SUCCESS
//     -------------------------
    @Test
    void shouldValidateUserToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/validate")
                        .param("token", "dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Email was validated successfully."));
    }
}
