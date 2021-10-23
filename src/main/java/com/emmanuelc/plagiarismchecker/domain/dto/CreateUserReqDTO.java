package com.emmanuelc.plagiarismchecker.domain.dto;

import com.emmanuelc.plagiarismchecker.domain.models.enumerations.RoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateUserReqDTO {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid email")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private RoleEnum role;
}
