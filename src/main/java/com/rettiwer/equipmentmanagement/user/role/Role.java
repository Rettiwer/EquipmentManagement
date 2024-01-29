package com.rettiwer.equipmentmanagement.user.role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Enumerated(EnumType.STRING)
    private UserRole name;

    @RequiredArgsConstructor
    public enum UserRole {
        ROLE_EMPLOYEE("ROLE_EMPLOYEE"),
        ROLE_SUPERVISOR("ROLE_SUPERVISOR"),
        ROLE_ADMIN("ROLE_ADMIN");

        public final String name;
    }
}
