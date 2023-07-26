package com.example.onlineartstore.config;



import com.zaxxer.hikari.util.DriverDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;
import java.util.Properties;


//@EnableWebSecurity
//@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {


//    matcher - сопоставитель, соответствие правилам, путям, паттернам

//    @Bean("h2DataSource")
//    public DataSource getH2DataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScripts("schema.sql") //если убрать эту строку, то схема будет сама сгенерирована
//                .build();
//    }

//    @Bean
//    public DataSource getDataSource() {
//        Properties properties = new Properties();
//        DriverDataSource sa = new DriverDataSource("jdbc:h2:file:~/testdb", "org.h2.Driver", properties, "sa", "");
//        return sa;
//    }


    // РАБОТАЕТ!

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
        jdbcUserDetailsManager.createUser(user);
        jdbcUserDetailsManager.createUser(admin);
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
                .formLogin()
                .and()
                //.csrf().disable() //отключить систему безопасности csrf()
                .build(); //mvcMatchers("/privatePage") здесь обязательная авторизация

    }

}

// Спроба!

//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final DataSource dataSource;
//
//    public WebSecurityConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public DataSource getDataSource() {
//        return dataSource;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests(authorize -> authorize
//                        .mvcMatchers("/**", "/singup/**", "/css/**", "/images/**").permitAll()
//                        .mvcMatchers("/privatePage/**").authenticated()
//                        .mvcMatchers("/mainAdminPage/**").hasRole("ADMIN")
//                        .mvcMatchers("/adminPage/**").hasRole("ADMIN")
//                        .mvcMatchers("/updateCategory/**").hasRole("ADMIN")
//                        .mvcMatchers("/updateAuthor/**").hasRole("ADMIN")
//                        .mvcMatchers("/updatePainting/**").hasRole("ADMIN")
//                        .mvcMatchers("/adminPageBets/**").hasRole("ADMIN")
//                        .mvcMatchers("/allBets/**").hasRole("ADMIN")
//                        .mvcMatchers("/adminPageUsers/**").hasRole("ADMIN")
//                        .mvcMatchers("/userPage/**", "/infoPage/**").hasRole("USER")
//                        .mvcMatchers("/participateAuction/**").hasRole("USER")
//                        .mvcMatchers("/updateUserDetailClient/**").hasRole("USER")
//                        .mvcMatchers("/victoryPage/**").hasRole("USER")
//                        .anyRequest().denyAll())
//                .formLogin();
//    }
//
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsManager getUserDetailsService(DataSource dataSource) {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("$2a$10$QgcqI.FMy8Z73QH1Fk/Eqeix.oBu41ejFnDwSOrgtG4IHG9fInHIm") //хэшированный пароль admin
//                .roles("ADMIN", "USER")
//                .build();
//        UserDetails user = User.builder()
//                .username("user")
//                .password("$2a$10$9zIdGinlpulUYzYPeqwZTO.mOD0uKOWiofG5Rj8kGazk.XGxTlEX6") //хэшированный пароль user
//                .roles("USER")
//                .build();
//        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);//для хранения данных в базе Н2
//        jdbcUserDetailsManager.createUser(user);
//        jdbcUserDetailsManager.createUser(admin);
//        return jdbcUserDetailsManager;
//    }
//
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(getDataSource())
//                .passwordEncoder(getPasswordEncoder());
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/images/**");
//    }
//
//}




