package com.emmanuelc.plagiarismchecker.service.impl;

import com.emmanuelc.plagiarismchecker.domain.dto.CreateUserReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.LoginReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.LoginRespDTO;
import com.emmanuelc.plagiarismchecker.domain.mapper.UserMapper;
import com.emmanuelc.plagiarismchecker.domain.models.BlacklistedToken;
import com.emmanuelc.plagiarismchecker.domain.models.User;
import com.emmanuelc.plagiarismchecker.exceptions.AppCustomException;
import com.emmanuelc.plagiarismchecker.repository.BlacklistedTokenRepo;
import com.emmanuelc.plagiarismchecker.repository.UserRepo;
import com.emmanuelc.plagiarismchecker.security.JwtTokenUtil;
import com.emmanuelc.plagiarismchecker.service.UserService;
import com.emmanuelc.plagiarismchecker.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final AuthenticationManager authManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final BlacklistedTokenRepo blacklistedTokenRepo;

    @Override
    public void registerUser(CreateUserReqDTO createUserReqDTO) {
        Optional<User> optionalUser = userRepo.findByEmail(createUserReqDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new AppCustomException("User with email already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        final User user = userMapper.toEntity(createUserReqDTO);
        userRepo.save(user);
    }

    @Override
    public LoginRespDTO loginUser(LoginReqDTO loginReqDTO) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(loginReqDTO.getEmail(), loginReqDTO.getPassword()));
        final User user = userRepo.findByEmail(loginReqDTO.getEmail()).orElseThrow();
        final String token = jwtTokenUtil.createToken(user.getEmail());
        return new LoginRespDTO() {
            {
                this.setId(user.getId());
                this.setToken(AppConstants.JWT_TOKEN_PREFIX + token);
            }
        };
    }

    @Override
    public void blacklistToken(String token) {
        if (token.startsWith(AppConstants.JWT_TOKEN_PREFIX)) {
            token = token.substring(7);
        }

        if (!jwtTokenUtil.validateToken(token)) {
            return;
        }

        BlacklistedToken blacklistedToken = blacklistedTokenRepo.findByToken(token);
        if (blacklistedToken != null) {
            return;
        }
        blacklistedToken = new BlacklistedToken();
        blacklistedToken.setExpiration(jwtTokenUtil.getTokenExpiration(token));
        blacklistedToken.setToken(token);

        blacklistedTokenRepo.save(blacklistedToken);
    }
}
