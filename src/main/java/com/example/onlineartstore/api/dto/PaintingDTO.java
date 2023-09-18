package com.example.onlineartstore.api.dto;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Author;
import com.example.onlineartstore.entity.Category;
import com.example.onlineartstore.entity.Painting;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull; // нужный пакет!
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class PaintingDTO {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    private LocalDate published;

    private String imageLink;

    @NotNull
    @NotBlank
    private String size;

    @NotNull
    @NotBlank
    private String material;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private BigDecimal price;

    private Boolean sold;

    @NotNull
    private Integer categoryId;
    @NotNull
    private Integer authorId;
    //картина может быть еще не подвязана под аукцион null
    private Integer auctionId;

    public Painting toEntity(Category category, Author author, Auction auction, String imageLink) {
        Painting painting = new Painting(title, published, imageLink, size, material, description, price, sold);
        painting.setCategory(category);
        painting.setAuthor(author);
        painting.setAuction(auction);

        return painting;
    }

}