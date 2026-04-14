package hn.shadowcore.mercadox.oauth.service;

import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.entity.model.auth.Role;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import hn.shadowcore.mercadox.library.entity.model.enums.RoleName;
import hn.shadowcore.mercadox.library.entity.request.RegisterRequestDto;
import hn.shadowcore.mercadox.library.jpa.repository.OrganizationRepository;
import hn.shadowcore.mercadox.library.jpa.repository.RoleRepository;
import hn.shadowcore.mercadox.library.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final OrganizationRepository orgRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(RegisterRequestDto requestDto) {

        final UUID orgId = UUID.fromString(requestDto.orgId());
        if(orgRepository.existsById(orgId)) {

            Organization organization = orgRepository.findById(orgId)
                    .orElseThrow(() -> new ResourceNotFoundException("Org was not found for ID: " + orgId));

            Role userRole = roleRepository.findByNameIgnoreCase(RoleName.USER.getValue())
                    .orElseThrow(() -> new ResourceNotFoundException
                            ("Role was not found for name: " + RoleName.USER.getValue()));

            User user = User.builder()
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .username(requestDto.username())
                    .password(passwordEncoder.encode(requestDto.password()))
                    .firstName(requestDto.firstName().trim())
                    .lastName(requestDto.lastName().trim())
                    .email(requestDto.email())
                    .enabled(false) // Missing email verification
                    .userType(requestDto.userType())
                    .organization(organization)
                    .userRoles(Set.of(Role.builder().build()))
                    .build();

            return userRepository.save(user);
        }
        throw new IllegalArgumentException("Organization is not active.");
    }


}
