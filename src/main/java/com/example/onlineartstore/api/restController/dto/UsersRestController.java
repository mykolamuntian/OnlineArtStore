package com.example.onlineartstore.api.restController.dto;

import com.example.onlineartstore.api.dto.UserDTO;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.error.ErrorResponse;
import com.example.onlineartstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/adminPageUsers/api/v3/users")
@RestController
public class UsersRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    List<User> list() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<User> show(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            return ResponseEntity.of(userRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}") //http://localhost:8080/adminPageUsers/api/v3/users/2 для update
    ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody @Validated UserDTO userDTO) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User variable = foundUser.get();

            User user = userDTO.toEntity();

            variable.setUsername(user.getUsername());
            variable.setPassword(user.getPassword());
            return ResponseEntity.of(Optional.of(userRepository.save(variable)));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult) { //полный объект User для @OneToOne User and UserDetail
        if (!bindingResult.hasErrors()) {
            User saved = userDTO.toEntity();
            saved.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            try {
                saved = userRepository.save(saved);
                System.out.println("userId = " + saved.getId());

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(e.getMessage()));
            }
            URI uri = URI.create("/adminPageUsers/api/v3/users/" + saved.getId());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest()
                .body(bindingResult.getAllErrors());
    }

    @DeleteMapping("/{id}") //http://localhost:8080/adminPageUsers/api/v3/users/2
    ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
