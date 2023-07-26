package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.entity.UserDetail;
import com.example.onlineartstore.error.ErrorResponse;
import com.example.onlineartstore.repository.UserDetailRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/userPage")
@RequiredArgsConstructor
public class UserPageController {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return "User not find!";
        }
        User user = optionalUser.get();
        model.addAttribute("user", user);
        model.addAttribute("userId", id);
        if (user.getUserDetail() != null) {
            model.addAttribute("userDetail", userDetailRepository.getReferenceById(user.getUserDetail().getId()));
        } else {
            model.addAttribute("userDetail", new UserDetail());
        }
        return "userPage";
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

    @GetMapping("/userDetails/{id}")
    @ResponseBody
    public ResponseEntity<?> showUserDetail(@PathVariable Integer id) {
        Optional<UserDetail> optionalUserDetail = userDetailRepository.findById(id);
        if (optionalUserDetail.isPresent()) {
            return ResponseEntity.ok(optionalUserDetail.get());
        }
        return ResponseEntity.badRequest().body("UserDetail not found!");
    }

    @PostMapping("/{id}")
    public String saveUserDetail(@PathVariable Integer id, @Valid UserDetail userDetail, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userRepository.findById(id).orElse(null));
            model.addAttribute("userId", id);
            model.addAttribute("userDetail", userDetail);
            return "userPage";
        }
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUserDetail(userDetail);
            userRepository.save(user);
            return "redirect:/" + id;
        }

        return "redirect:/";
    }

}
