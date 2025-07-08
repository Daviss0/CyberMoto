package com.cybermoto.controller;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import com.cybermoto.service.UserService;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

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

    @GetMapping ("/manage-users")
    public String manageUsers(Model model, Principal principal ) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("userLogado", user);
        List<User> users = userRepository.findAll();
        model.addAttribute("usuarios", users);
        return "manage-users";
}

    @GetMapping("/add-users")
    public String addUsers(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("userLogado", user);
        model.addAttribute("user", new User());
        return "add-users";
    }

    @GetMapping("edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("userData", user);
        return "edit-user";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("userData") User user, Model model) {

        System.out.println(">>> Recebido para edição: ID = " + user.getId());

        try {
        userService.saveUser(user);
            System.out.println(">>> Usuário salvo com sucesso. Redirecionando para manage-users.");
            return "redirect:/admin/manage-users";
        }
        catch (Exception e) {
            System.out.println(">>> Erro ao salvar usuário:");
            e.printStackTrace(); // mostra o erro no console

            model.addAttribute("userData", user);
            model.addAttribute("erro", e.getMessage());
            return "edit-user";
        }
    }

    @PostMapping("/user-added")
    public String addUser (@ModelAttribute ("user") User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/admin/manage-users"; // redireciona para o metodo manageUsers que carrega a lista de usuarios

        } catch (Exception e) {

            model.addAttribute("user", user); // reenvia os dados preenchidos
            model.addAttribute("erro", e.getMessage()); // passa a mensagem de erro para a view
            return "add-users"; //redireciona para a mesma pagina
        }

    }
        @PostMapping("/toggle-status/{id}")
        public String toggleUserStatus(@PathVariable Long id){
        userService.toggleUserStatus(id);
          return "redirect:/admin/manage-users";
        }
}
