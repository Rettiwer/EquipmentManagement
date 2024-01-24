package com.rettiwer.equipmentmanagement.authentication;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private List<String> roles;
}