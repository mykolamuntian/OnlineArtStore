package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Category;
import com.example.onlineartstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/updateCategory")
@RequiredArgsConstructor
@Controller
public class UpdateCategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).orElseThrow());
        model.addAttribute("categoryId", id);
        return "updateCategory";
    }

    @GetMapping("/categories/{id}")
    @ResponseBody
    public ResponseEntity<?> showCategory(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return ResponseEntity.ok(optionalCategory.get());
        }
        return ResponseEntity.badRequest().body("Category not found!");
    }

}
