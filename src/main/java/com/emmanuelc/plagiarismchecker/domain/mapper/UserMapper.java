package com.emmanuelc.plagiarismchecker.domain.mapper;

import com.emmanuelc.plagiarismchecker.domain.dto.CreateUserReqDTO;
import com.emmanuelc.plagiarismchecker.domain.models.Role;
import com.emmanuelc.plagiarismchecker.domain.models.User;
import com.emmanuelc.plagiarismchecker.repository.RoleRepo;
import com.emmanuelc.plagiarismchecker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder encoder;

    private final RoleRepo roleRepo;

    public User toEntity(CreateUserReqDTO createUserReqDTO) {
        if (createUserReqDTO == null) {
            return null;
        }
        final Role role = roleRepo.findByName(createUserReqDTO.getRole());
        final User user = new User()
                .setFirstname(createUserReqDTO.getFirstname())
                .setLastname(createUserReqDTO.getLastname())
                .setEmail(createUserReqDTO.getEmail())
                .setPassword(encoder.encode(createUserReqDTO.getPassword()))
                .addRole(role);
        return user;
    }
}
