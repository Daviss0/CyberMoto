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

import java.security.Principal;
import java.util.List;
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


    @GetMapping("/home")
    public String home() {
        return "home";
    }

@GetMapping ("/manage-users")
public String manageUsers(Model model ) {
        List<User> users = userRepository.findAll();
        model.addAttribute("usuarios", users);
        return "manage-users";
}
}
