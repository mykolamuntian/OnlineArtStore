package com.example.onlineartstore.controller;

import com.example.onlineartstore.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EncoderController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/encode")
    @ResponseBody
    String encode(@RequestParam String rawPassword) {
        return rawPassword + " -> " + passwordEncoder.encode(rawPassword);
    }
    // в браузере    http://localhost:8080/encode?rawPassword=admin  результат зашифрованного пароля "admin" : admin -> $2a$10$QgcqI.FMy8Z73QH1Fk/Eqeix.oBu41ejFnDwSOrgtG4IHG9fInHIm
    // в браузере    http://localhost:8080/encode?rawPassword=user  результат зашифрованного пароля "user" : user -> $2a$10$9zIdGinlpulUYzYPeqwZTO.mOD0uKOWiofG5Rj8kGazk.XGxTlEX6
    // в браузере    http://localhost:8080/encode?rawPassword=alina7799  результат зашифрованного пароля "alina7799" : alina7799 -> $2a$10$NK3U0QyTt2/8vVCmNQTHY.Ryx70WUVGmp0cS3uHlaErHZ1i5yrak6

}
