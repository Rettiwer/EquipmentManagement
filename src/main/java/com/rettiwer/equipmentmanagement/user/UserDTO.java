package com.rettiwer.equipmentmanagement.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Integer id;
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
    @Pattern(regexp=".+@.+\\..+")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain one lowercase letter."),
            @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain one upper letter."),
            @Pattern(regexp = "(?=.*[!@#$%^&*+=?-_()/\"\\.,<>~`;:]).+", message ="Password must contain one special character."),
    })
    private String password;

    private Integer supervisorId;
    @NotNull
    @Valid
    private List<RoleDTO> roles;
    private List<ItemDTO> items;
    private List<UserDTO> employees;

    public UserDTO(String firstname, String lastname, String email, String password, Integer supervisorId, List<RoleDTO> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.supervisorId = supervisorId;
        this.roles = roles;
    }
}
