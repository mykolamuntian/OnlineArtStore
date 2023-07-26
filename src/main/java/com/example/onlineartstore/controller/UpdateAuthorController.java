package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Author;
import com.example.onlineartstore.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/updateAuthor")
@RequiredArgsConstructor
@Controller
public class UpdateAuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        model.addAttribute("author", authorRepository.findById(id).orElseThrow());
        model.addAttribute("authorId", id);
        return "updateAuthor";
    }

    @GetMapping("/authors/{id}")
    @ResponseBody
    public ResponseEntity<?> showAuthor(@PathVariable Integer id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            return ResponseEntity.ok(optionalAuthor.get());
        }
        return ResponseEntity.badRequest().body("Author not found!");
    }

}
