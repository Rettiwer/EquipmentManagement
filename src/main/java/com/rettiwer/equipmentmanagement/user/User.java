package com.rettiwer.equipmentmanagement.user;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rettiwer.equipmentmanagement.authentication.token.Token;
import com.rettiwer.equipmentmanagement.item.Item;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;

    @Column(name = "lastname", columnDefinition = "VARCHAR(255) NOT NULL, FULLTEXT KEY fullname_fulltext (firstname, lastname)")
    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;

    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "supervisor_employee",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "supervisor_id")})
    @JsonBackReference
    private User supervisor;


    @OneToMany(mappedBy = "supervisor")
    private List<User> employees;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).toList();
    }

    public boolean hasRole(Role.UserRole userRole) {
        return roles.stream().anyMatch(role -> role.getName() == userRole);
    }

    public boolean hasAnyRole(List<Role.UserRole> userRoles) {
        return roles.stream().noneMatch(role -> userRoles.contains(role.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}