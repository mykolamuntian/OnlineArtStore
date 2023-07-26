package com.example.onlineartstore.service;

import com.example.onlineartstore.entity.Auction;
import com.example.onlineartstore.entity.Role;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.repository.RoleRepository;
import com.example.onlineartstore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class RolesAndUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public Role saveRole(Role role, String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow();
        role.addUserRole(user);
        roleRepository.save(role);
        userRepository.save(user);
        return role;
    }
}
