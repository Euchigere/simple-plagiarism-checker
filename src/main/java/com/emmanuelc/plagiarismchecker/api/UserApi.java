package com.emmanuelc.plagiarismchecker.api;

import com.emmanuelc.plagiarismchecker.domain.dto.ApiResponse;
import com.emmanuelc.plagiarismchecker.domain.dto.CreateUserReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.LoginReqDTO;
import com.emmanuelc.plagiarismchecker.domain.dto.LoginRespDTO;
import com.emmanuelc.plagiarismchecker.service.UserService;
import com.emmanuelc.plagiarismchecker.util.ControllerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody @Valid CreateUserReqDTO createUserReqDTO) {
        userService.registerUser(createUserReqDTO);
        return ControllerUtil.buildResponseEntity(null, "User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRespDTO>> login(@RequestBody @Valid LoginReqDTO loginReqDTO) {
        final LoginRespDTO loginRespDTO = userService.loginUser(loginReqDTO);
        return ControllerUtil.buildResponseEntity(loginRespDTO);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<ApiResponse<Object>> logOut(@PathVariable String token) {
        userService.blacklistToken(token);
        return ControllerUtil.buildResponseEntity(null, "Deleted successfully");
    }
}
