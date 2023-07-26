package com.example.onlineartstore.api.restController.dto;

import com.example.onlineartstore.api.dto.UserDetailDTO;
import com.example.onlineartstore.entity.User;
import com.example.onlineartstore.entity.UserDetail;
import com.example.onlineartstore.error.ErrorResponse;
import com.example.onlineartstore.repository.UserDetailRepository;
import com.example.onlineartstore.repository.UserRepository;
import com.example.onlineartstore.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/adminPageUsers/api/v3/userDetails")
@RestController
public class UserDetailsRestController {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final UserDetailService userDetailService;


    @GetMapping
    List<UserDetail> list() {
        return userDetailRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDetail> showUserDetail(@PathVariable Integer id) {
        if (userDetailRepository.existsById(id)) {
            return ResponseEntity.of(userDetailRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}") //http://localhost:8080/adminPageUsers/api/v3/userDetails/6
    ResponseEntity<UserDetail> updateUserDetail(@PathVariable Integer id, @RequestBody @Validated UserDetailDTO userDetailDTO) {
        Optional<UserDetail> foundUserDetail = userDetailRepository.findById(id);
        if (foundUserDetail.isPresent()) {
            UserDetail variable = foundUserDetail.get();

            Optional<User> optionalUser = userRepository.findById(userDetailDTO.getUserId());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            UserDetail userDetail = userDetailDTO.toEntity(optionalUser.get());

            variable.setFirstName(userDetail.getFirstName());
            variable.setLastName(userDetail.getLastName());
            variable.setBirthDate(userDetail.getBirthDate());
            variable.setEmail(userDetail.getEmail());
            variable.setTelephone(userDetail.getTelephone());
            userDetailRepository.save(variable);
            return ResponseEntity.ok().body(variable);
        }
        return ResponseEntity.notFound().build();
    }



    @PostMapping
    ResponseEntity<?> createUserDetail(@RequestBody @Validated UserDetailDTO userDetailDTO, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            Optional<User> optionalUser = userRepository.findById(userDetailDTO.getUserId());
            UserDetail saved;
            try {
                if (optionalUser.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                User user = optionalUser.get();

                // вывод сообщения для пользователя, response.status === 409 или 400
                if(user.getUserDetail() != null){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("My message in Java Script!");
                }
                saved = userDetailRepository.save(userDetailDTO.toEntity(user));
                saved = userDetailService.saveDetails(saved, user.getUsername());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(e.getMessage()));
            }
            URI uri = URI.create("/adminPageUsers/api/v3/userDetails/" + saved.getId());
            return ResponseEntity.created(uri).build();

        }
        return ResponseEntity.badRequest()
                .body(bindingResult.getAllErrors());
    }


    @DeleteMapping("/{id}") //http://localhost:8080/adminPageUsers/api/v3/userDetails/4
    ResponseEntity<?> deleteUserDetail(@PathVariable Integer id) {
        if (userDetailRepository.existsById(id)) {
            userDetailRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
