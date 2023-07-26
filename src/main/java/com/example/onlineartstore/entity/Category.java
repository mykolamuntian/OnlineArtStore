package com.example.onlineartstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Categories")
@ToString(exclude = "paintings")
@EqualsAndHashCode(exclude = "paintings")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String name;


    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}) //cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    private List<Painting> paintings = new ArrayList<>();

    public void addPainting(Painting... paintings) {
        for (Painting painting : paintings) {
            painting.setCategory(this);
            this.paintings.add(painting);
        }
    }

}
