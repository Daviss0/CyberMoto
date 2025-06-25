package com.cybermoto.controller;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import com.cybermoto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @PostMapping("logar")
    public String logar(@RequestParam String email,
                        @RequestParam String password,
                        Model model ) {
        User usuario = userService.loginUser(email, password);

        if (usuario != null) {
            model.addAttribute("Usuario", usuario);
            return "home";
        }
        else {
            model.addAttribute("erro", "E-mail ou senha incorretos");
            return "login";
        }

    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
