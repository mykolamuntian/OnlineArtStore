package com.example.onlineartstore.api.restController.dto;

import com.example.onlineartstore.api.dto.AuthorDTO;
import com.example.onlineartstore.entity.Author;
import com.example.onlineartstore.repository.AuthorRepository;
import com.example.onlineartstore.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/adminPage/api/v1/authors")
@RequiredArgsConstructor
public class AuthorsRestController {

    private final AuthorRepository authorRepository;
    private final ImageUploadService imageUploadService;

    @GetMapping
    List<Author> list() {
        return authorRepository.findAll();
    }


    @GetMapping("/{id}")
    ResponseEntity<Author> showAuthor(@PathVariable Integer id) {
        if (authorRepository.existsById(id)) {
            return ResponseEntity.of(authorRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/createAuthor")
    ResponseEntity<?> create(@RequestPart("file") MultipartFile imageFileAuthor, @RequestPart AuthorDTO authorDTO) {
        try {
            String imageLink = imageUploadService.uploadImage(imageFileAuthor);
            if (imageLink == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            Author saved = authorRepository.save(authorDTO.toEntity(imageLink));
            return ResponseEntity
                    .created(URI.create("/adminPage/api/v1/authors/" + saved.getId()))
                    .build();
        } catch (Throwable throwable) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(throwable);
        }
    }

    @PutMapping("/update/{authorId}")
    ResponseEntity<Author> updateAuthor(@PathVariable Integer authorId, @RequestPart(value = "file", required = false)
            MultipartFile imageFileAuthor, @RequestPart AuthorDTO authorDTO) {
        Optional<Author> foundAuthors = authorRepository.findById(authorId);
        if (foundAuthors.isPresent()) {
            Author authorToUpdate = foundAuthors.get();
            if (imageFileAuthor != null) {
                String imageLink = imageUploadService.uploadImage(imageFileAuthor);
                if (imageLink == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                authorToUpdate.setImageLink(imageLink);
            }
            Author author = authorDTO.toEntity(authorDTO.getImageLink());
            authorToUpdate.setPseudonym(author.getPseudonym());
            authorToUpdate.setFirstName(author.getFirstName());
            authorToUpdate.setSurname(author.getSurname());
            authorToUpdate.setBirthDate(author.getBirthDate());
            authorToUpdate.setAwards(author.getAwards());
            return ResponseEntity.of(Optional.of(authorRepository.save(authorToUpdate)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}

