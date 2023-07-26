package com.example.onlineartstore.entity;

import com.example.onlineartstore.api.classAnnotation.Phone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "UsersDetails")
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private String email;

    @NonNull
    @NotNull
    @NotBlank
    @Phone
    private String telephone;


    @OneToOne(mappedBy = "userDetail") // не ставить аннотацию @JsonIgnore, а то не увижу username
    @NotNull // UserDetail не может существовать отдельно от User!!! Они жестко связаны!Важная аннотация!!!!
    private User user;

}
