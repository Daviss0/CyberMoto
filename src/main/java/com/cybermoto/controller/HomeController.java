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
      User user = userRepository.findByEmail(principal.getName()).orElse(null);
      model.addAttribute("userLogado", user);
      return "home";
    }
}
