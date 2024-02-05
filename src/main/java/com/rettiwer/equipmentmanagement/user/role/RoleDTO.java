package com.rettiwer.equipmentmanagement.user.role;

import com.rettiwer.equipmentmanagement.user.role.validator.ValidateUserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull
    @ValidateUserRole
    private String name;
}
