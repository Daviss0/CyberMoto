package com.cybermoto.controller;

import com.cybermoto.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.cybermoto.entity.Client;
import com.cybermoto.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientService clientService;

    @GetMapping("login-client")
    public String loginClient(Model model)throws Exception {
        return "login-client";
    }

    @GetMapping("add-client")
    public String addClient(Model model){
    try {
      model.addAttribute("client", new Client());
      return "add-client";
    }
    catch(Exception e) {
      model.addAttribute("error", "Erro ao carregar a pagina" + e.getMessage());
      return "error-page";
    }
    }

    @PostMapping("client-added")
    public String clientAdded(@Valid @ModelAttribute ("client") Client client,
                              BindingResult bindingResult,
                              Model model) {
      if(bindingResult.hasErrors()) {
          return "add-client";
      }
        try {
          clientService.addClient(client);
          return "redirect:/client/login-client";
        }
        catch(IllegalArgumentException e) {
          model.addAttribute("error", e.getMessage());
          return "add-client";
        }
    }


    @GetMapping("/mainPage")
    public String mainPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/client/login-client";
        }
        Client client = clientRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("clientName", client.getName());
        return "mainPage";
    }
}
