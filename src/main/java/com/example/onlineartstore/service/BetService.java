package com.example.onlineartstore.service;

import com.example.onlineartstore.api.dto.AuctionStatusDTO;
import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Bet;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.BetRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final SseEmitterService emitterService;
    //SseEmitterService — это настраиваемая служба, которая управляет списком объектов SseEmitter и отправляет события всем имитерам. Метод sendEventToAll отправляет объект AuctionStatusDTO в качестве данных события всем клиентам, прослушивающим поток.

    public String ruleBet(Bet bet) {
        Auction auction = bet.getAuction();
        if (auction.getEndDate().isBefore(LocalDateTime.now())) {
            return "Auction has ended!";
        }
        BigDecimal currentHighestBet = auction.getCurrentBet();
        BigDecimal minimumBet = currentHighestBet.add(BigDecimal.valueOf(500));
        if (bet.getAmountMoney().compareTo(minimumBet) < 0) {
            return "The amount of the bet must be at least 500 UAH higher than the current bet!";
        }

        // Делаю предыдущую ставку неактивной
        List<Bet> bets = auction.getBetsAuction();
        for (Bet b : bets) {
            if (b.getAmountMoney().compareTo(currentHighestBet) == 0) {   // 0 - если числа равны
                b.setActive(false);
                betRepository.save(b);
                break;
            }
        }

        // Активирую новую текущую ставку
        bet.setActive(true);
        betRepository.save(bet);

        // Устанавливаю новую текущую ставку как самую высокую ставку и сохраняю аукцион
        auction.setCurrentBet(bet.getAmountMoney());
        auctionRepository.save(auction);

        return null;
    }

    // все предыдущие ставки и ошибки false
    public void invalidatePreviousBets(Auction auction, User user) {
        List<Bet> previousBets = betRepository.findByAuctionAndUserAndActiveTrueOrderByAmountMoneyDesc(auction, user);
        for (Bet bet : previousBets) {
            bet.setActive(false);
        }
        betRepository.saveAll(previousBets);
    }

    // сортировка ставки
    public List<Bet> getBetsByAuction(Auction auction) {
        return betRepository.findAllByAuctionOrderByAmountMoneyDesc(auction);
    }

    public void closeAuction(Auction auction) {
        System.out.println("Closing auction with ID: " + auction.getId());

        List<Bet> bets = getBetsByAuction(auction);
        if (!bets.isEmpty()) {
            Bet highestBid = bets.get(0);
            highestBid.setActive(false);
            auction.setCurrentBet(highestBid.getAmountMoney());
            auction.setWinner(highestBid.getUser().getUsername());
        }
        auction.setActive(false);
        auctionRepository.save(auction);

        System.out.println("Updated auction information: " + auction.toString());

        AuctionStatusDTO auctionStatus = new AuctionStatusDTO(true);
        emitterService.sendEventToAll(auction.getId(), "auctionUpdate", auctionStatus);
    }

    public boolean isAuctionEnded(int auctionId) {
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        if (optionalAuction.isEmpty()) {
            throw new IllegalArgumentException("Auction not found with id " + auctionId);
        }
        return optionalAuction.get().getEndDate().isBefore(LocalDateTime.now());
    }

    public boolean isBetAmountValid(Auction auction, BigDecimal betAmount) {
        BigDecimal currentHighestBet = auction.getCurrentBet();
        BigDecimal minimumBet = currentHighestBet.add(BigDecimal.valueOf(500));
        return betAmount.compareTo(minimumBet) >= 0;
    }

}
