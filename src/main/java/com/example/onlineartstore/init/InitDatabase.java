package com.example.onlineartstore.init;

import com.example.onlineartstore.entity.*;
import com.example.onlineartstore.repository.*;
import com.example.onlineartstore.service.RolesAndUserService;
import com.example.onlineartstore.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
//@Component
@RequiredArgsConstructor
public class InitDatabase implements CommandLineRunner {

    private final PaintingRepository paintingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final AuctionRepository auctionRepository;
    private final UserDetailService userDetailService;
    private final RolesAndUserService rolesAndUserService;
    final private RoleRepository roleRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        UserDetail userDetail1 = new UserDetail("Alina", "Muntian", LocalDate.of(1986, Month.MARCH, 5), "muntian@gmail.com", "095-797-51-67");
        userDetailService.saveDetails(userDetail1, "user");
        UserDetail userDetail2 = new UserDetail("Alina", "Muntian", LocalDate.of(1986, Month.MARCH, 5), "muntian@gmail.com", "095-797-51-67");
        userDetailService.saveDetails(userDetail2, "admin");

    }
}
