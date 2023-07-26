package com.example.onlineartstore.api.dto;

import lombok.Data;

@Data
public class AuctionStatusDTO {
    private boolean ended;

    public AuctionStatusDTO(boolean ended) {
        this.ended = ended;
    }

}

