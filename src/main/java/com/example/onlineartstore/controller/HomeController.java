package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.*;
import com.example.onlineartstore.service.AuctionAddUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final PaintingRepository paintingRepository;
    private final AuctionRepository auctionRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final UserDetailsService userDetailsService;
    private final AuctionAddUserService auctionAddUserService;

    @GetMapping
    String generalPage(Model model) {
        model.addAttribute("paintings", paintingRepository.findAll());
        return "generalPage";
    }

    @GetMapping("/privatePage")
    String privatePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findUserByUsername(auth.getName());
        if(userOptional.isEmpty()){
            return "Not find USER!";
        }
        User user = userOptional.get();
        Integer userId = user.getId();
        userDetailsService.loadUserByUsername(user.getUsername());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("user", userRepository.findById(user.getId()));
        model.addAttribute("userId", userId);
        model.addAttribute("auctions", auctionRepository.findAll());
        model.addAttribute("paintings", paintingRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "privatePage";
    }


    @GetMapping("/infoPage")
    String infoPage(Model model) {
        model.addAttribute("userDetail", userDetailRepository.findAll());
        return "infoPage";
    }

}

