package com.example.onlineartstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@ToString(exclude = {"betsUsers","comments", "participationInAuctions", "userDetail"})//была рекурсия из-за поля userDetail, так как User связан с UserDetail @OneToOne
@EqualsAndHashCode(exclude = {"participationInAuctions", "roles", "userDetail"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    @Length(min = 3)
    private String username;

    @NotNull
    @NotBlank
    @Length(min = 6)
    private String password;


    private Boolean enabled;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    //cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    private List<Comment> comments = new ArrayList<>();


    public void addComment(Comment comment) {
        comment.setUser(this);
        comments.add(comment);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    //cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    //каскадное обновление данных, если author сохраняется, сохраняются м данные painting
    private List<Bet> betsUsers = new ArrayList<>();


    @JsonIgnore
    @OneToOne   // не пишу аннотацию @NotNull так как User спокойно существует без UserDetail!!!!!
    private  UserDetail userDetail;

    @JsonIgnore
    @ManyToMany
    private Set<Auction> participationInAuctions = new HashSet<>();

    //добавила роли
    @JsonIgnore
    @ManyToMany(mappedBy = "usersRole", cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        role.addUserRole(this);
        roles.add(role);
    }

    public void addAuction(Auction auction) {
        participationInAuctions.add(auction);
    }

    public User(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}
