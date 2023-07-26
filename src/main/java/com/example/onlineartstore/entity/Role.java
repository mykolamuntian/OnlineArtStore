package com.example.onlineartstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(exclude = "usersRole")
@EqualsAndHashCode(exclude = "usersRole")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String name;

    @JsonIgnore
    @ManyToMany
    private Set<User> usersRole = new HashSet<>();

    public void addUserRole(User user) {
        usersRole.add(user);
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
