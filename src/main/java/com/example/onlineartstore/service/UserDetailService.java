package com.example.onlineartstore.service;

import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.entity.UserDetail;
import com.example.onlineartstore.repository.UserDetailRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    @Transactional
    public UserDetail saveDetails(UserDetail userDetail, String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow();
        userDetail.setUser(user);
        userDetailRepository.save(userDetail);
        user.setUserDetail(userDetail);
        userRepository.save(user); //здесь дебажить userRepository.findAll() чтоб увидеть связь @OneToOne User and UserDetail
        return userDetail;
    }
    // UserDetailService для @OneToOne User and UserDetail!!!!
}
