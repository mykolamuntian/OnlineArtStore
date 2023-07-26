package com.example.onlineartstore.api.dto;

import com.example.onlineartstore.entity.Auction;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionDTO {
    @NotNull
    private String titleAuction;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private BigDecimal startingPrice;

    @NotNull
    private BigDecimal currentBet;

    @NotNull
    private Boolean active;

    private String winner;


    public Auction toEntity() {
        return new Auction(titleAuction, startDate, endDate, startingPrice, currentBet, active, winner);
    }
}
