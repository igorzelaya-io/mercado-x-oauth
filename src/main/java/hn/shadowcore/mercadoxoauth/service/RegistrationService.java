package hn.shadowcore.mercadoxoauth.service;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.KafkaTopic;
import hn.shadowcore.mercadoxlibrary.entity.ports.incoming.RegistrationUseCase;
import hn.shadowcore.mercadoxlibrary.entity.response.dto.EmailEventDto;
import hn.shadowcore.mercadoxlibrary.entity.response.dto.VerificationToken;
import hn.shadowcore.mercadoxlibrary.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService implements RegistrationUseCase {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;

    @Override
    public void validateUser(String token) {

        final VerificationToken verificationToken = Optional.ofNullable(redisTemplate
                .opsForValue().get("verification:" + token))
                .filter(VerificationToken.class::isInstance)
                .map(VerificationToken.class::cast)
                .orElseThrow(() -> new ResourceNotFoundException
                        (String.format("Token '%s' was not found for value: ", token)));

        if(LocalDateTime.now().isBefore(verificationToken.expiresAt().toLocalDateTime())) {

            final User deactivatedUser = userRepository
                    .findByIdAndEnabled(UUID.fromString(verificationToken.userId()), false)
                    .orElseThrow(() -> new ResourceNotFoundException(String
                            .format("User with supposed ID: '%s' was not found.", verificationToken.userId())));

            deactivatedUser.setEnabled(true);
            userRepository.save(deactivatedUser);
            return;
        }
        throw new IllegalArgumentException(String.format("Token '%s' was not found for value: ", token));
    }

    @Override
    public void registerUser(User user) {

        EmailEventDto<String> verificationEvent = new EmailEventDto<>
                ("USER_REGISTERED", user.getEmail(), user.getFirstName(), createVerificationToken(user));

        kafkaTemplate.send(KafkaTopic.USER_REGISTRATION, verificationEvent);

    }

    private String createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(),
                Timestamp.valueOf(LocalDateTime.now().plusDays(1)), user.getId().toString());

        final String redisKey = new StringBuilder("verification:")
                .append(verificationToken.id()).toString();

        redisTemplate.opsForValue().set(redisKey, verificationToken, Duration.ofDays(1));
        return "http://localhost:8081/api/v1/auth/verify?token=" + verificationToken.id();
    }


}
