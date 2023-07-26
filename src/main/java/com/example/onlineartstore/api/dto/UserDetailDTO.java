package com.example.onlineartstore.api.dto;

import com.example.onlineartstore.api.classAnnotation.Phone;
import com.example.onlineartstore.entity.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class UserDetailDTO {
    @NonNull
    @NotNull
    @NotBlank
    private String firstName;

    @NonNull
    @NotNull
    @NotBlank
    private String lastName;

    @NonNull
    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NonNull
    @NotNull
    @NotBlank
    @Email
    private String email;

    @NonNull
    @NotNull
    @NotBlank
    @Phone
    private String telephone;

    @NotNull
    private Integer userId;

    public UserDetail toEntity(User user) {
        UserDetail userDetail = new UserDetail(firstName,lastName,birthDate,email,telephone);
        userDetail.setUser(user);
        return userDetail;
    }
}
