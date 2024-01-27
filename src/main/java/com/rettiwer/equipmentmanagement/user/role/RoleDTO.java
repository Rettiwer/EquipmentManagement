package com.rettiwer.equipmentmanagement.user.role;

import com.rettiwer.equipmentmanagement.user.role.validator.ValidUserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull
    @ValidUserRole
    private String name;
}
