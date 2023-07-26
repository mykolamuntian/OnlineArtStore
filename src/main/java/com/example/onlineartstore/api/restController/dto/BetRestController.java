package com.example.onlineartstore.api.restController.dto;

import com.example.onlineartstore.api.dto.AuctionStatusDTO;
import com.example.onlineartstore.api.dto.BetDTO;
import com.example.onlineartstore.entity.*;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.BetRepository;
import com.example.onlineartstore.repository.UserRepository;
import com.example.onlineartstore.service.BetService;
import com.example.onlineartstore.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.net.URI;
import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/v2/participateAuction") // /api/v2/participateAuction/{id}(аукциона)/bet/id(ставки)
@RestController
public class BetRestController {

    private final BetRepository betRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BetService betService;
    private final SseEmitterService sseEmitterService;

    @GetMapping("{id}/bets")
    List<Bet> list(@PathVariable int id, Principal principal) {
        return auctionRepository.findById(id)
                .orElseThrow()
                .getBetsAuction()
                .stream()
                .filter(bet -> bet.getUser().getUsername().equals(principal.getName()))
                .sorted(Comparator.comparing(Bet::getCreatedDate).reversed())
                .toList();
    }

//    @GetMapping("/{id}/sse")
//    public SseEmitter auctionSSE(@PathVariable int id) {
//        // SSE - события отправленные сервером, SSE позволяет серверу отправлять обновления на клиентскую сторону в режиме реального времени!
//        SseEmitter emitter = new SseEmitter();
//        sseEmitterService.addEmitter(emitter, id);
//        boolean ended = betService.isAuctionEnded(id);
//        if (ended) {
//            auctionRepository.findById(id).ifPresent(betService::closeAuction);
//        }
//        return emitter;
//    }

    @GetMapping("{auctionId}/bet/{id}")
    ResponseEntity<Bet> show(@PathVariable int auctionId, @PathVariable Integer id, Principal principal) {
        Optional<Bet> optionalBet = betRepository.findById(id);
        if (optionalBet.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Bet bet = optionalBet.get();
        if (bet.getAuction().getId() != auctionId) {
            return ResponseEntity.badRequest().build();
        }
        if (bet.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.of(optionalBet);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Bet> update(@PathVariable Integer id, @RequestBody Bet bet) {
        Optional<Bet> foundBet = betRepository.findById(id);
        if (foundBet.isPresent()) {
            Bet bet1 = foundBet.get();
            bet1.setCreatedDate(bet.getCreatedDate());
            bet1.setAmountMoney(bet.getAmountMoney());
            bet1.setActive(bet.getActive());
            return ResponseEntity.of(Optional.of(betRepository.save(bet1)));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/sse") // http://localhost:8080/api/v2/participateAuction/1/sse
    public SseEmitter auctionSSE(@PathVariable Integer id) {
        SseEmitter emitter = new SseEmitter();
        try {
        sseEmitterService.addEmitter(emitter, id);
        boolean ended = betService.isAuctionEnded(id);
        if (ended) {
            auctionRepository.findById(id).ifPresent(betService::closeAuction);
        }
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

//    @PostMapping("/{id}") // http://localhost:8080/api/v2/participateAuction/1
//    public ResponseEntity<?> createBet(@PathVariable Integer id, @RequestBody BetDTO betDTO, Principal principal) {
//        try {
//            Optional<User> optionalUser = userRepository.findUserByUsername(principal.getName());
//            if (optionalUser.isEmpty()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            Optional<Auction> optionalAuction = auctionRepository.findById(id);
//            if (optionalAuction.isEmpty()) {
//                return ResponseEntity.badRequest().body("Auction not found!");
//            }
//
//            Auction auction = optionalAuction.get();
//            User user = optionalUser.get();
//            if (betService.isAuctionEnded(auction.getId())) {
//                betService.closeAuction(auction);
//                return ResponseEntity.badRequest().body("Auction has ended!");
//            }
//
//            BigDecimal betAmount = betDTO.getAmountMoney();
//            if (!betService.isBetAmountValid(auction, betAmount)) {
//                return ResponseEntity.badRequest().body("The amount of the bet must be at least 500 UAH higher than the current bet!");
//            }else{
//                Bet saved = betRepository.save(betDTO.toEntity(auction, user));
//                betService.invalidatePreviousBets(auction, user);
//
//                String errorMessage = betService.ruleBet(saved);
//                if (errorMessage != null) {
//                    return ResponseEntity.badRequest().body(errorMessage);
//                }else{
//                    return ResponseEntity.created(URI.create("/api/v2/participateAuction/" + id + "/bet/" + saved.getId())).build();
//                }
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    @PostMapping("/{id}") // http://localhost:8080/api/v2/participateAuction/1
    public ResponseEntity<?> createBet(@PathVariable Integer id, @RequestBody BetDTO betDTO, Principal principal) {
        try {
            Optional<User> optionalUser = userRepository.findUserByUsername(principal.getName());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Optional<Auction> optionalAuction = auctionRepository.findById(id);
            if (optionalAuction.isEmpty()) {
                return ResponseEntity.badRequest().body("Auction not found!");
            }

            Auction auction = optionalAuction.get();
            User user = optionalUser.get();
            BigDecimal betAmount = betDTO.getAmountMoney();

            if (betService.isAuctionEnded(auction.getId())) {
                betService.closeAuction(auction); // точно закрывает аукцион!
                return ResponseEntity.badRequest().body("Auction has ended!");
            }

            if (!betService.isBetAmountValid(auction, betAmount)) {
                return ResponseEntity.badRequest().body("The amount of the bet must be at least 500 UAH higher than the current bet!");
            }else{
                Bet saved = betRepository.save(betDTO.toEntity(auction, user));
                betService.invalidatePreviousBets(auction, user);

                String errorMessage = betService.ruleBet(saved);
                if (errorMessage != null) {
                    return ResponseEntity.badRequest().body(errorMessage);
                }else{
                    return ResponseEntity.created(URI.create("/api/v2/participateAuction/" + id + "/bet/" + saved.getId())).build();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) {
        if (betRepository.existsById(id)) {
            betRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}


