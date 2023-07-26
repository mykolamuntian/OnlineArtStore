package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Category;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.entity.UserDetail;
import com.example.onlineartstore.repository.UserDetailRepository;
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

@RequestMapping("/updateUserDetail")
@RequiredArgsConstructor
@Controller
public class UpdateUserDetailController {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        model.addAttribute("userDetail", userDetailRepository.findById(id).orElseThrow());
        model.addAttribute("userDetailId", id);
        return "updateUserDetail";
    }

    @GetMapping("/userDetails/{id}")
    @ResponseBody
    public ResponseEntity<?> showUserDetail(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        Optional<UserDetail> optionalUserDetail = userDetailRepository.findById(id);
        if (optionalUserDetail.isPresent()) {
            return ResponseEntity.ok(optionalUserDetail.get());
        }
        return ResponseEntity.badRequest().body("UserDetail not found!");
    }

}
