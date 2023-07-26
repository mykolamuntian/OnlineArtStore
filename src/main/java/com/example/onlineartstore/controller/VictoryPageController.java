package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/victoryPage")
@RequiredArgsConstructor
public class VictoryPageController {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return "User not find!";
        }
        User user = optionalUser.get();
        model.addAttribute("user", user);
        model.addAttribute("userId", id);
        List<Auction> auctions = auctionRepository.findAll();
        model.addAttribute("auctions", auctions);
        for (Auction auction : auctions) {
            if(auction.getWinner().equals(user.getUsername())){
                model.addAttribute("auction", auction);
            }
        }
        return "victoryPage";
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
