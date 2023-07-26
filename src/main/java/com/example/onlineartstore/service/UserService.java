package com.example.onlineartstore.service;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.User;

import java.util.Set;

public interface UserService {
    void addUserToAuction(Integer userId, Integer auctionId);

    void removeUserFromAuction(Integer userId, Integer auctionId);

    Set<User> getUsersForAuction(Integer auctionId);

    Set<Auction> getAuctionsForUser(Integer userId);
}
