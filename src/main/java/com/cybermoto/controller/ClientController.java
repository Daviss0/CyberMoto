package com.cybermoto.controller;

import com.cybermoto.entity.Address;
import com.cybermoto.entity.Product;
import com.cybermoto.enums.UF;
import com.cybermoto.repository.AddressRepository;
import com.cybermoto.repository.ProductRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AddressRepository addressRepository;

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

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/client/mainPage";
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model, Principal principal) {
      List <Product> products = productRepository.findAll();
      model.addAttribute("products", products);

      if(principal != null) {
          Client client = clientRepository.findByEmail(principal.getName())
                  .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
          model.addAttribute("client", client);
      }
      return "mainPage";
    }

    @GetMapping("/account")
    public String account (Model model, Principal principal) {
        Client client = clientRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("client", client);

        if(!model.containsAttribute("newAddress")) {
            model.addAttribute("newAddress", new Address());
        }
        model.addAttribute("ufs", UF.values());
        return "account";
    }

    @PostMapping("/account")
    public String updateAccount (@ModelAttribute("client") Client formClient, Principal principal) {
      Client client = clientRepository.findByEmail(principal.getName())
              .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        client.setName(formClient.getName());
        client.setBirth(formClient.getBirth());
        client.setGender(formClient.getGender());
        client.setPassword(formClient.getPassword());

        clientRepository.save(client);
        return "redirect:/client/account?success";
    }

    @PostMapping("/account/address")
    public String addAddress (@Valid @ModelAttribute("newAddress") Address newAddress,
                              BindingResult br,
                              Principal principal,
                              RedirectAttributes ra) {
        Client client = clientRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.newAddress", br);
            ra.addFlashAttribute("newAddress", newAddress);
            return "redirect:/client/account#new-address";
        }

        newAddress.setClient(client);
        addressRepository.save(newAddress);

        ra.addFlashAttribute("msgAddress", "Mensagem adicionada com sucesso!");
        return "redirect:/client/account#new-address";
    }
}
