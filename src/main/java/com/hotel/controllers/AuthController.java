package com.hotel.controllers;

import com.hotel.entities.UserEntity;
import com.hotel.services.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(AuthController.ROOT)
public class AuthController {

    public static final String ROOT = "/api/user";

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Secured("ROLE_USER")
    public void signup(HttpServletResponse http,
                     @RequestParam String email,
                     @RequestParam String firstname,
                     @RequestParam String lastname,
                     @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date birthdate,
                     @RequestParam Character sex,
                     @RequestParam String password) throws IOException {
        userService.create(new UserEntity(firstname, lastname, email, birthdate, sex, new BCryptPasswordEncoder().encode(password)));
        http.sendRedirect("/hotels");
    }

    @GetMapping("/delete")
    @Secured("ROLE_ADMIN")
    public void delete(@RequestParam String email) {
        userService.deleteByEmail(email);
    }

    @PostMapping("/profile-edit")
    @Secured("ROLE_USER")
    public void edit(HttpServletResponse http,
                         @RequestParam String email,
                         @RequestParam String firstname,
                         @RequestParam String lastname,
                         @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date birthdate,
                         @RequestParam Character sex,
                         @RequestParam String password) throws IOException {
        userService.update(new UserEntity(firstname, lastname, email, birthdate, sex, new BCryptPasswordEncoder().encode(password)));
        http.sendRedirect("/hotels");
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<UserEntity> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public UserEntity getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserEntity create(@RequestBody UserEntity userEntity) {
        return userService.create(userEntity);
    }

    @PutMapping
    public UserEntity update(@RequestBody UserEntity userEntity) {
        return userService.update(userEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}
