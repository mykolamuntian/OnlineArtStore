package com.example.onlineartstore.api.dto;

import com.example.onlineartstore.entity.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {
    @NotNull
    @NotBlank
    @Column(unique = true)
    @Length(min = 3)
    private String username;

    @NotNull
    @NotBlank
    @Length(min = 6)
    private String password;


    public User toEntity() {
        return new User(username, password, true);
    }

}
