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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@Slf4j
@Component
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

        String imagePath1 = "artLana";
        String imagePath2 = "alina";
        Author author1 = new Author("artLana", "Svetlana", "Vovk", LocalDate.of(1957, Month.OCTOBER, 18), "Many awards", imagePath1);
        authorRepository.save(author1);
        Author author2 = new Author("artMaster", "Alina", "Muntian", LocalDate.of(1986, Month.MARCH, 05), "Many awards", imagePath2);
        authorRepository.save(author2);

        User star = new User("Star", "$2a$10$OEKBPlQJhYFgSQ7PqKXfKulkZnj/FnozqYP8E7T2ro.3YPi8fxlGa", true); // 12345
        User niceCat = new User("NiceCat", "$2a$10$Y9HP/PX0shaO51dWyHunM.MxUkGvNLyLn0zn/uIYLu7eXNnZqCSZ6", true); //2222
        User lionKing = new User("LionKing", "$2a$10$OEKBPlQJhYFgSQ7PqKXfKulkZnj/FnozqYP8E7T2ro.3YPi8fxlGa", true); // 12345
        User good = new User("Good", "$2a$10$OEKBPlQJhYFgSQ7PqKXfKulkZnj/FnozqYP8E7T2ro.3YPi8fxlGa", true); // 12345
        userRepository.save(star);
        userRepository.save(niceCat);
        userRepository.save(lionKing);
        userRepository.save(good);
        Role role1 = new Role("ADMIN");
        Role role2 = new Role("USER");
        roleRepository.save(role1);
        roleRepository.save(role2);
        rolesAndUserService.saveRole(role2, "Star");
        rolesAndUserService.saveRole(role2, "NiceCat");
        rolesAndUserService.saveRole(role2, "LionKing");


        Category category1 = new Category("acrylic");
        Category category2 = new Category("watercolor");
        Category category3 = new Category("oil");
        Category category4 = new Category("gouache");
        Category category5 = new Category("coal");

        Comment comment1 = new Comment("This is a beautiful picture");

        Auction auction1 = new Auction("Auction of connoisseurs of beauty!",
                LocalDateTime.of(2023, Month.JULY, 17, 10, 15),
                LocalDateTime.of(2023, Month.JULY, 18, 21, 30),
                BigDecimal.valueOf(3000),
                BigDecimal.valueOf(3500),
                true, "Who is the winner?");
        auctionRepository.save(auction1);
        Auction auction2 = new Auction("Easter auction!", LocalDateTime.of(2023, Month.JULY, 17, 11, 10), LocalDateTime.of(2023, Month.AUGUST, 30, 11, 10), BigDecimal.valueOf(1000), BigDecimal.valueOf(1500), true, "Who is the winner?");
        auctionRepository.save(auction2);

        Auction auction3 = new Auction("Green Spring!", LocalDateTime.of(2023, Month.JULY, 1, 12, 00), LocalDateTime.of(2023, Month.AUGUST, 30, 15, 55), BigDecimal.valueOf(1000), BigDecimal.valueOf(1500), false, "Who is the winner?");
        auctionRepository.save(auction3);

        Auction auction4 = new Auction("Hot Summer!", LocalDateTime.of(2023, Month.JULY, 1, 12, 00), LocalDateTime.of(2023, Month.AUGUST, 3, 14, 00), BigDecimal.valueOf(1000), BigDecimal.valueOf(1500), false, "Who is the winner?");
        auctionRepository.save(auction4);

        //загрузка изображения картин
        String imagePath3 = "ukrainian";
        String imagePath4 = "butterfly";
        String imagePath5 = "rainbow";
        String imagePath6 = "kaleidoscope";
        String imagePath7 = "rooster";
        String imagePath8 = "letter";
        String imagePath9 = "sunflowers";

        Painting painting1 = new Painting("Girl with a violin", LocalDate.of(2022, Month.APRIL, 15), imagePath3, "60x100cm", "canvas", "The picture is made in bright colors, yellow-green color will provide you with a good mood", BigDecimal.valueOf(5000), false);
        author1.addComment(comment1);

        Painting painting2 = new Painting("Equilibrium", LocalDate.of(2021, Month.JANUARY, 20), imagePath4, "100x100cm", "canvas", "This picture is like a bright comet ", BigDecimal.valueOf(8000), false);

        Painting painting3 = new Painting("Flecks of sunlight", LocalDate.of(2022, Month.JULY, 25), imagePath5, "40x50cm", "canvas", "This picture is beautiful ", BigDecimal.valueOf(3000), false);

        Painting painting4 = new Painting("Christmas melody", LocalDate.of(2022, Month.DECEMBER, 16), imagePath6, "40x60cm", "canvas", "The Ukrainian girl who plays the violin embodies the hopes of all Ukrainians for peace and a bright future ", BigDecimal.valueOf(7000), false);

        Painting painting5 = new Painting("Let's stand like a rooster from Borodyanka", LocalDate.of(2023, Month.JANUARY, 8), imagePath7, "50x60cm", "canvas", "About the stability and indomitability of the people of Ukraine ", BigDecimal.valueOf(4500), false);
        Painting painting6 = new Painting("Love letter", LocalDate.of(2020, Month.MARCH, 17), imagePath8, "100x100cm", "canvas", "Love letter written with the brightest feelings", BigDecimal.valueOf(2000), false);
        Painting painting7 = new Painting("Sunflowers", LocalDate.of(2021, Month.SEPTEMBER, 22), imagePath9, "100x100cm", "canvas", "Sunflowers - flowers filled with the sun", BigDecimal.valueOf(4000), false);


        author1.addPainting(painting1, painting2, painting3, painting4, painting5, painting6,painting7);

        category1.addPainting(painting1, painting2, painting3, painting4);
        category2.addPainting(painting6,painting7);
        category3.addPainting(painting5);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);

        auction1.addPainting(painting1);
        paintingRepository.save(painting1);
        auction2.addPainting(painting2);
        auction2.addPainting(painting3);
        paintingRepository.save(painting2);
        paintingRepository.save(painting3);

        UserDetail userDetail1 = new UserDetail("Alina", "Muntian", LocalDate.of(1986, Month.MARCH, 5), "muntian@gmail.com", "095-797-51-67");
        userDetailService.saveDetails(userDetail1, "user");
        UserDetail userDetail2 = new UserDetail("Alina", "Muntian", LocalDate.of(1986, Month.MARCH, 5), "muntian@gmail.com", "095-797-51-67");
        userDetailService.saveDetails(userDetail2, "admin");
        UserDetail userDetail3 = new UserDetail("Anna", "Petrova", LocalDate.of(2003, Month.DECEMBER, 11), "petrova@gmail.com", "099-941-78-55");
        userDetailService.saveDetails(userDetail3, "Star");
        UserDetail userDetail4 = new UserDetail("Victoria", "Omelchenko", LocalDate.of(2001, Month.APRIL, 2), "omelchenko@gmail.com", "067-831-81-27");
        userDetailService.saveDetails(userDetail4, "NiceCat");
        UserDetail userDetail5 = new UserDetail("Roman", "Solovey", LocalDate.of(1986, Month.MAY, 22), "solovey@gmail.com", "063-834-41-26");
        userDetailService.saveDetails(userDetail5, "LionKing");

    }
}
