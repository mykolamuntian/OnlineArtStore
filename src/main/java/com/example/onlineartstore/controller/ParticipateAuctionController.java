package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.UserRepository;
import com.example.onlineartstore.service.AuctionAddUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.Optional;

@RequestMapping("/participateAuction")
@RequiredArgsConstructor
@Controller
public class ParticipateAuctionController {
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionAddUserService auctionAddUserService;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model, Principal principal) {
        Optional<User> userOptional = userRepository.findUserByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return "User not found!";
        }
        User user = userOptional.get();
        Integer userId = user.getId();
        model.addAttribute("user", userRepository.findById(userId));
        model.addAttribute("userId", userId);
        model.addAttribute("auction", auctionRepository.findById(id).orElseThrow());
        model.addAttribute("auctionId", id);
        return "participateAuction";
    }

    @GetMapping("/auctions/{id}")
    @ResponseBody
    public ResponseEntity<?> showAuction(@PathVariable Integer id) {
        Optional<Auction> optionalAuction = auctionRepository.findById(id);
        if (optionalAuction.isPresent()) {
            return ResponseEntity.ok(optionalAuction.get());
        }
        return ResponseEntity.badRequest().body("Auction not found!");
    }

    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> participateAuction(@PathVariable Integer id, Principal principal) {
        Optional<User> userOptional = userRepository.findUserByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }
        User user = userOptional.get();
        Integer userId = user.getId();
        auctionAddUserService.addUserToAuction(userId, id);
        Optional<Auction> auctionOptional = auctionRepository.findById(id);
        if (auctionOptional.isPresent()) {
            Auction auction = auctionOptional.get();
            auction.addAuctionParticipants(user);
            auctionRepository.save(auction);
        }
        return ResponseEntity.ok("User added as a participant");
    }

}
