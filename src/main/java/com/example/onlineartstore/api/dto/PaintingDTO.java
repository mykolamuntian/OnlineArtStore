package com.example.onlineartstore.api.dto;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Author;
import com.example.onlineartstore.entity.Category;
import com.example.onlineartstore.entity.Painting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull; // нужный пакет!
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
public class PaintingDTO {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    private LocalDate published;

    private String imageLink; // каталог, в котором загруженные изображения будут храниться на сервере!

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

    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }

    public Painting toEntity(Category category, Author author, Auction auction, String imageLink) {
        Painting painting = new Painting(title, published, imageLink, size, material, description, price, sold);
        painting.setCategory(category);
        painting.setAuthor(author);
        painting.setAuction(auction);

        return painting;
    }

//    public String uploadImage() throws IOException {
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
//            Path destinationPath = Paths.get("src/main/resources/static/images/" + fileName);
//            Files.copy(imageFile.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
//            return "/static/images/" + fileName;
//        }
//        return null;
//    }
}