package com.example.onlineartstore.api.dto;

import com.example.onlineartstore.entity.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BetDTO {
    @NotNull
    @NotBlank
    private BigDecimal amountMoney;

    public Bet toEntity(Auction auction, User user) {
        Bet bet = new Bet(LocalDateTime.now(), amountMoney, true); //time
        bet.setAuction(auction);
        bet.setUser(user);
        return bet;
    }
}
