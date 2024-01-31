package com.rettiwer.equipmentmanagement.authentication;

import com.rettiwer.equipmentmanagement.authentication.validator.UniqueEmail;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotEmpty
    @Size(min=3, max = 32)
    @Pattern(regexp = "[A-Za-z]+")
    private String firstname;

    @NotNull
    @NotEmpty
    @Size(min=3, max = 32)
    @Pattern(regexp = "[A-Za-z]+")
    private String lastname;

    @NotNull
    @NotEmpty
    @Email
    @UniqueEmail
    @Pattern(regexp=".+@.+\\..+")
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain one lowercase letter."),
            @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain one upper letter."),
            @Pattern(regexp = "(?=.*[!@#$%^&*+=?-_()/\"\\.,<>~`;:]).+", message ="Password must contain one special character."),
    })
    private String password;

    @NotNull
    @NotNull
    @Valid
    private List<RoleDTO> roles;

    private Integer supervisorId;
}