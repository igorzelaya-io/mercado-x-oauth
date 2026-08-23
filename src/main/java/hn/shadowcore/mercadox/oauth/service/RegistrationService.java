package hn.shadowcore.mercadox.oauth.service;

import hn.shadowcore.mercadox.library.entity.avro.EmailRecipient;
import hn.shadowcore.mercadox.library.entity.avro.NotificationTemplateName;
import hn.shadowcore.mercadox.library.entity.avro.UserRegistrationEmailEvent;
import hn.shadowcore.mercadox.library.entity.kafka.KafkaTopic;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import hn.shadowcore.mercadox.library.entity.ports.incoming.RegistrationUseCase;
import hn.shadowcore.mercadox.library.entity.response.dto.VerificationTokenDto;
import hn.shadowcore.mercadox.library.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
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

        final VerificationTokenDto verificationToken = Optional.ofNullable(redisTemplate
                .opsForValue().get("verification:" + token))
                .filter(VerificationTokenDto.class::isInstance)
                .map(VerificationTokenDto.class::cast)
                .orElseThrow(() -> new ResourceNotFoundException
                        (String.format("Token '%s' was not found for value: ", token)));

        if(LocalDateTime.now().isBefore(verificationToken.expiresAt().toLocalDateTime())) {

            final User deactivatedUser = userRepository
                    .findDisabledUserById(verificationToken.userId());

            deactivatedUser.setEnabled(true);
            userRepository.save(deactivatedUser);
            return;
        }
        throw new IllegalArgumentException(String.format("Token '%s' was not found for value: ", token));
    }

    @Override
    public void registerUser(User user) {

        EmailRecipient recipient = EmailRecipient.newBuilder()
                .setName(user.getFirstName())
                .setEmail(user.getEmail())
                .build();

        UserRegistrationEmailEvent event = UserRegistrationEmailEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventSubject("Email Confirmation!")
                .setEmailTemplate(NotificationTemplateName.USER_VALIDATION_TEMPLATE)
                .setRecipients(List.of(recipient))
                .setPayload(createVerificationToken(user))
                .setTimestamp(Instant.now())
                .build();

        kafkaTemplate.send(KafkaTopic.USER_REGISTRATION, event);

    }

    private String createVerificationToken(User user) {
        VerificationTokenDto verificationToken = new VerificationTokenDto(UUID.randomUUID().toString(),
                Timestamp.valueOf(LocalDateTime.now().plusDays(1)), user.getId().toString());

        final String redisKey = new StringBuilder("verification:")
                .append(verificationToken.id()).toString();

        redisTemplate.opsForValue().set(redisKey, verificationToken, Duration.ofDays(1));
        return "http://localhost:8081/api/v1/auth/verify?token=" + verificationToken.id();
    }


}
