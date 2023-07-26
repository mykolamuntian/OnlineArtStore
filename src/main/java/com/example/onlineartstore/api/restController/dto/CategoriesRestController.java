package com.example.onlineartstore.api.restController.dto;

import com.example.onlineartstore.entity.Category;
import com.example.onlineartstore.error.ErrorResponse;
import com.example.onlineartstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/adminPage/api/v1/categories")
@RequiredArgsConstructor
public class CategoriesRestController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    List<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Category> showCategory(@PathVariable Integer id) {
        if (categoryRepository.existsById(id)) {
            return ResponseEntity.of(categoryRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category saved = categoryRepository.save(category);
            return ResponseEntity
                    .created(URI.create("/adminPage/api/v1/categories/" + saved.getId()))
                    .build();
        } catch (Throwable throwable) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(throwable);
        }
    }

    @PutMapping("/{id}")   //http://localhost:8080/adminPage/api/v1/categories/5 для update
    ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category variable = optionalCategory.get();
            variable.setName(category.getName());
            return ResponseEntity.of(Optional.of(categoryRepository.save(variable)));
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Category not found"));
    }

//    @DeleteMapping("/{id}")
//    ResponseEntity<?> delete(@PathVariable Integer id) {
//        if (categoryRepository.existsById(id)) {
//            categoryRepository.deleteById(id);
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.delete(optionalCategory.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("This category not found!"));
    }


}
