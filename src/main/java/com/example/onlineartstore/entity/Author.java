package com.example.onlineartstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String pseudonym;
    @NonNull
    private String firstName;
    @NonNull
    private String surname;
    @NonNull
    private LocalDate birthDate;
    @NonNull
    private String awards;
    @NonNull
    private String imagePath;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
    @ToString.Exclude //cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    //каскадное обновление данных, если author сохраняется, сохраняются м данные painting
    private List<Painting> paintingsAuthor = new ArrayList<>();

    public void addPainting(Painting... paintings) {
        for (Painting painting : paintings) {
            painting.setAuthor(this);
            this.paintingsAuthor.add(painting);
        }
    }

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<Comment> commentsAuthor = new ArrayList<>();

    public void addComment(Comment comment){
        comment.setAuthor(this);
        commentsAuthor.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Author author = (Author) o;
        return id != null && Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
