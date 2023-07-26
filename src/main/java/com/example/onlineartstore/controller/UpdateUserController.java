package com.example.onlineartstore.controller;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@RequestMapping("/updateUser")
@RequiredArgsConstructor
@Controller
public class UpdateUserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElseThrow());
        model.addAttribute("userId", id);
        return "updateUser";
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<?> showUser(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        return ResponseEntity.badRequest().body("User not found!");
    }

}
