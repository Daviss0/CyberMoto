package com.cybermoto.controller;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }

        userRepository.findByEmail(principal.getName()).ifPresent(user -> {
            model.addAttribute("userLogado", user);
        });

        return "home";
    }


}
