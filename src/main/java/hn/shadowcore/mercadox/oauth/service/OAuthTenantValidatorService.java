package hn.shadowcore.mercadox.oauth.service;

import hn.shadowcore.mercadox.context.validator.AnonymousTenantValidator;
import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.jpa.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
=======
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
>>>>>>> cc207632b944b49b49938d6169adfe3aedb968b0
@RequiredArgsConstructor
public class OAuthTenantValidatorService implements AnonymousTenantValidator {

    private final OrganizationRepository organizationRepository;
    @Override
    public boolean validate(String orgId) {
        return organizationRepository
                .findById(UUID.fromString(orgId))
                .map(Organization::getEnabled)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Organization was not found or is not active."));
    }

}
