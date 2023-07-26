package com.example.onlineartstore.service;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.AuctionRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuctionAddUserService implements UserService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    @Override
    @Transactional
    //которая гарантирует, что вся операция выполняется в рамках одной транзакции, поэтому в случае возникновения каких-либо ошибок транзакция будет отменена, а база данных останется в согласованном состоянии
    public void addUserToAuction(Integer userId, Integer auctionId) {
        User user = userRepository.getReferenceById(userId);
        Auction existingAuction = auctionRepository.getReferenceById(auctionId);
        existingAuction.getAuctionParticipants().add(user);
        user.getParticipationInAuctions().add(existingAuction);
        auctionRepository.save(existingAuction);
        userRepository.save(user);
    }


    @Override
    @Transactional
    public void removeUserFromAuction(Integer userId, Integer auctionId) {
        User user = userRepository.getReferenceById(userId);
        Auction auction = auctionRepository.getReferenceById(auctionId);
        user.getParticipationInAuctions().remove(auction);
        auction.getAuctionParticipants().remove(user);
        userRepository.save(user);
        auctionRepository.save(auction);
    }

    @Override
    public Set<User> getUsersForAuction(Integer auctionId) {
        Auction auction = auctionRepository.getReferenceById(auctionId);
        return auction.getAuctionParticipants();
    }

    @Override
    public Set<Auction> getAuctionsForUser(Integer userId) {
        User user = userRepository.getReferenceById(userId);
        return user.getParticipationInAuctions();
    }
}

// Для @ManyToMany    (User and Role) !!!