package hn.shadowcore.mercadox.oauth.mapper;

import hn.shadowcore.mercadox.library.entity.model.auth.Role;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import hn.shadowcore.mercadox.library.entity.model.auth.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    @Mapping(target = "authorities", expression = "java(mapRolesToAuthorities(user.getUserRoles()))")
    UserDetailsImpl toUserDetails(User user);

    default Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
