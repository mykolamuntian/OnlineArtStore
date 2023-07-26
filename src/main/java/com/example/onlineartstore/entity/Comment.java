package com.example.onlineartstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String content;


    @ManyToOne
    private User user;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Painting painting;

    @ManyToOne
    private Auction auction;


}
