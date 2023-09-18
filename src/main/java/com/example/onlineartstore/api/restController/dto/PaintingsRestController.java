package com.example.onlineartstore.api.restController.dto;

import com.example.onlineartstore.api.dto.PaintingDTO;
import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Author;
import com.example.onlineartstore.entity.Category;
import com.example.onlineartstore.entity.Painting;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.AuthorRepository;
import com.example.onlineartstore.repository.CategoryRepository;
import com.example.onlineartstore.repository.PaintingRepository;
import com.example.onlineartstore.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/adminPage/api/v1/paintings")
@RestController
public class PaintingsRestController {

    private final PaintingRepository paintingRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final AuctionRepository auctionRepository;
    private final ImageUploadService imageUploadService;

    // READ ALL Paintings
    @GetMapping
    List<Painting> list() {
        return paintingRepository.findAll();
    }

    // READ ONE Painting
    @GetMapping("/{id}")
    ResponseEntity<Painting> show(@PathVariable Integer id) {
        if (paintingRepository.existsById(id)) {
            return ResponseEntity.of(paintingRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    //@RequestPart - використовується для отримання двох частин запиту: MultipartFile та MyMetadata
    @PostMapping("/create")
    public ResponseEntity<?> createPainting(@RequestPart("file") MultipartFile imageFile,
                                            @RequestPart PaintingDTO paintingDTO) {
        try {
            String imageLink = imageUploadService.uploadImage(imageFile);
            if (imageLink == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            Optional<Category> optionalCategory = categoryRepository.findById(paintingDTO.getCategoryId());
            Optional<Author> optionalAuthor = authorRepository.findById(paintingDTO.getAuthorId());
            Optional<Auction> optionalAuction = auctionRepository.findById(paintingDTO.getAuctionId());
            if (optionalCategory.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if (optionalAuthor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if (optionalAuction.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Painting saved = paintingRepository.save(paintingDTO.toEntity(optionalCategory.get(), optionalAuthor.get(),
                    optionalAuction.get(), imageLink));

            return ResponseEntity
                    .created(URI.create("/adminPage/api/v1/paintings/" + saved.getId()))
                    .build();
        } catch (Throwable throwable) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(throwable);
        }
    }


    // UPDATE ONE Painting
    @PutMapping("/update/{paintingId}")
    ResponseEntity<Painting> updatePainting(@PathVariable Integer paintingId, @RequestPart(value = "file", required = false)
            MultipartFile imageFile, @RequestPart PaintingDTO paintingDTO) {
        Optional<Painting> foundPainting = paintingRepository.findById(paintingId);
        if (foundPainting.isPresent()) {
            Painting paintingToUpdate = foundPainting.get();

            Optional<Category> optionalCategory = categoryRepository.findById(paintingDTO.getCategoryId());
            Optional<Author> optionalAuthor = authorRepository.findById(paintingDTO.getAuthorId());
            Optional<Auction> optionalAuction = auctionRepository.findById(paintingDTO.getAuctionId());
            if (optionalCategory.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if (optionalAuthor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if (optionalAuction.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if (imageFile != null) {
                String imageLink = imageUploadService.uploadImage(imageFile);
                if (imageLink == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                paintingToUpdate.setImageLink(imageLink);
            }
            Painting painting = paintingDTO.toEntity(optionalCategory.get(), optionalAuthor.get(), optionalAuction.get(), paintingDTO.getImageLink());

            paintingToUpdate.setTitle(painting.getTitle());
            paintingToUpdate.setPublished(painting.getPublished());
            paintingToUpdate.setSize(painting.getSize());
            paintingToUpdate.setMaterial(painting.getMaterial());
            paintingToUpdate.setDescription(painting.getDescription());
            paintingToUpdate.setPrice(painting.getPrice());
            paintingToUpdate.setSold(painting.getSold());
            paintingToUpdate.setAuthor(painting.getAuthor());
            paintingToUpdate.setAuction(painting.getAuction());
            paintingToUpdate.setCategory(painting.getCategory());
            return ResponseEntity.of(Optional.of(paintingRepository.save(paintingToUpdate)));
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE Painting
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) {
        if (paintingRepository.existsById(id)) {
            paintingRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
