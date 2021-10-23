package com.emmanuelc.plagiarismchecker.security;

import com.emmanuelc.plagiarismchecker.domain.models.Role;
import com.emmanuelc.plagiarismchecker.domain.models.User;
import com.emmanuelc.plagiarismchecker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyUserDetailsServices implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email: '%s' not found", username)));
        return new MyUserDetails(user);
    }
}
