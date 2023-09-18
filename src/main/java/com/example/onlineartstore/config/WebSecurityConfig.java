package com.example.onlineartstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

    @Bean
    public UserDetailsManager getUserDetailsService(DataSource dataSource) {
        UserDetails admin = User.builder()
                .username("admin")
                .password("$2a$10$QgcqI.FMy8Z73QH1Fk/Eqeix.oBu41ejFnDwSOrgtG4IHG9fInHIm") //хэшированный пароль admin
                .roles("ADMIN", "USER")
                .build();
        UserDetails user = User.builder()
                .username("user")
                .password("$2a$10$9zIdGinlpulUYzYPeqwZTO.mOD0uKOWiofG5Rj8kGazk.XGxTlEX6") //хэшированный пароль user
                .roles("USER")
                .build();
        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);//для хранения данных в базе Н2
//        jdbcUserDetailsManager.createUser(user);
//        jdbcUserDetailsManager.createUser(admin);
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(); // кодирует пароль, есть защита!
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize

                        .mvcMatchers("/actuator/**").permitAll()
                        .mvcMatchers("/**", "/singup/**", "/css/**", "/images/**").permitAll()  //всем доступна главная страница и регистрация!!!
                        .mvcMatchers("/privatePage/**").authenticated() // mvcMatchers("/privatePage") здесь обязательная авторизация
                        .mvcMatchers("/mainAdminPage/**").hasRole("ADMIN") //только для роли ADMIN
                        .mvcMatchers("/adminPage/**").hasRole("ADMIN")
                        .mvcMatchers("/updateCategory/**").hasRole("ADMIN")
                        .mvcMatchers("/updateAuthor/**").hasRole("ADMIN")
                        .mvcMatchers("/updatePainting/**").hasRole("ADMIN")
                        .mvcMatchers("/adminPageBets/**").hasRole("ADMIN")
                        .mvcMatchers("/allBets/**").hasRole("ADMIN")
                        .mvcMatchers("/adminPageUsers/**").hasRole("ADMIN")
                        .mvcMatchers("/userPage/**", "/infoPage/**").hasRole("USER") //только для роли USER, но ADMIN тоже зайдет так как  UserDetails admin = User.builder()..roles("ADMIN", "USER")
                        .mvcMatchers("/participateAuction/**").hasRole("USER")
                        .mvcMatchers("/updateUserDetailClient/**").hasRole("USER")
                        .mvcMatchers("/victoryPage/**").hasRole("USER")
                        .anyRequest().denyAll()) //все остальные запросы запрещены!!!
//                .formLogin()
//                .and()
//                .build(); //mvcMatchers("/privatePage") здесь обязательная авторизация
                .formLogin()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().frameOptions().disable()
                .and()
                .build(); //mvcMatchers("/privatePage")

    }

}




