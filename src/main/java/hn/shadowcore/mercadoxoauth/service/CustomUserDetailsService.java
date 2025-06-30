package hn.shadowcore.mercadoxoauth.service;

import hn.shadowcore.mercadoxlibrary.jpa.repository.UserRepository;
import hn.shadowcore.mercadoxoauth.mapper.UserDetailsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    public CustomUserDetailsService(UserRepository userRepository, UserDetailsMapper userDetailsMapper) {
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    // Authentication is made by email instead of username.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsMapper.toUserDetails(userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException
                        (String.format("Email was not found for user: '%s'", username))));
    }
}
