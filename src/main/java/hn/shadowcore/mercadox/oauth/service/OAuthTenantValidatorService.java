package hn.shadowcore.mercadox.oauth.service;

import hn.shadowcore.mercadox.context.validator.AnonymousTenantValidator;
import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.jpa.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
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
