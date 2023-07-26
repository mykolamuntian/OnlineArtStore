package com.example.onlineartstore.repository;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Bet;
import com.example.onlineartstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Integer> {

    List<Bet> findAllByAuctionOrderByAmountMoneyDesc(Auction auction);

    List<Bet> findByAuctionAndUserAndActiveTrueOrderByAmountMoneyDesc(Auction auction, User user);
}
