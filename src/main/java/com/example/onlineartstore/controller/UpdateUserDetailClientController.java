package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.entity.UserDetail;
import com.example.onlineartstore.repository.UserDetailRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/updateUserDetailClient") ///updateUserDetailClient/{userDetailId}/{userId}
@RequiredArgsConstructor
public class UpdateUserDetailClientController {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    @GetMapping("/{userDetailId}/{userId}")
    String index(@PathVariable Integer userDetailId, @PathVariable Integer userId, Model model) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return "User not find!";
        }
        User user = optionalUser.get();
        if (!user.getUsername().equals(userDetailRepository.getReferenceById(userDetailId).getUser().getUsername())) {
            return "UserDetail does not match with User";
        }
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", userRepository.findUserByUsername(user.getUsername()).orElseThrow());
        model.addAttribute("userId", userId);
        model.addAttribute("userDetail", userDetailRepository.findById(userDetailId).orElseThrow());
        model.addAttribute("userDetailId", userDetailId);
        return "updateUserDetailClient";
    }

    @GetMapping("/userDetails/{id}")
    @ResponseBody
    public ResponseEntity<?> showUserDetail(@PathVariable Integer id) {
        Optional<UserDetail> optionalUserDetail = userDetailRepository.findById(id);
        if (optionalUserDetail.isPresent()) {
            return ResponseEntity.ok(optionalUserDetail.get());
        }
        return ResponseEntity.badRequest().body("UserDetail not found!");
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
