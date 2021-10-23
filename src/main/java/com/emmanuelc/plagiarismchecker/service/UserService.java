package com.emmanuelc.plagiarismchecker.service;

import com.emmanuelc.plagiarismchecker.domain.dto.CreateUserReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.LoginReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.LoginRespDTO;

public interface UserService {
    void registerUser(CreateUserReqDTO createUserReqDTO);

    LoginRespDTO loginUser(LoginReqDTO loginReqDTO);

    void blacklistToken(String token);
}
