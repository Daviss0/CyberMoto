package com.cybermoto.controller;

import com.cybermoto.entity.Cart;
import com.cybermoto.entity.Client;
import com.cybermoto.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @GetMapping
    public String view(@AuthenticationPrincipal(expression = "client") Client client, Model model) {
      Cart cart = cartService.getCart(client);
      model.addAttribute("cart", cart);
      return "cart";
  }

  @PostMapping("/add")
    public String addItem(@AuthenticationPrincipal(expression = "client") Client client,
                          @RequestParam Long productId,
                          @RequestParam(defaultValue = "1") int quantity) {
          cartService.addItem(client, productId, quantity);
          return "redirect:/cart";
  }

  @PostMapping("/update")
    public String update(@AuthenticationPrincipal(expression = "client")Client client,
                         @RequestParam Long itemId,
                         @RequestParam int quantity) {
      cartService.updateQuantity(client, itemId, quantity);
      return "redirect:/cart";
  }

  @PostMapping("/remove")
    public String remove(@AuthenticationPrincipal(expression = "client") Client client,
                         @RequestParam Long itemId) {
      cartService.removeItem(client, itemId);
      return "redirect:/cart";
  }

  @PostMapping("/clear")
    public String clear(@AuthenticationPrincipal(expression = "client") Client client) {
      cartService.clear(client);
      return "redirect:/cart";
  }
}
