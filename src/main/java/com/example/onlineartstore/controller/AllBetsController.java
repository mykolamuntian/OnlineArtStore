package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Bet;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.BetRepository;
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


@RequestMapping("/allBets")
@RequiredArgsConstructor
@Controller
public class AllBetsController {

    private final AuctionRepository auctionRepository;
    private final BetRepository betRepository;

    @GetMapping("/{id}")
    String index(@PathVariable Integer id, Model model) {
        Auction auction = auctionRepository.findById(id).orElseThrow();
        List<Bet> bets = betRepository.findAllByAuctionOrderByAmountMoneyDesc(auction);
        model.addAttribute("auction", auction);
        model.addAttribute("bets", bets);
        return "allBets";
    }

    @GetMapping("/bets/{id}")
    @ResponseBody
    public ResponseEntity<?> showBet(@PathVariable Integer id) {
        Optional<Bet> optionalBet = betRepository.findById(id);
        if (optionalBet.isPresent()) {
            return ResponseEntity.ok(optionalBet.get());
        }
        return ResponseEntity.badRequest().body("Bet not found!");
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

}
