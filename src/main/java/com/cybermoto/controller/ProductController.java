package com.cybermoto.controller;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import com.cybermoto.service.ImageProductService;
import com.cybermoto.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.cybermoto.entity.Product;
import com.cybermoto.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageProductService imageProductService;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/manage-products")
    public String showManageProducts(Model model, Principal principal) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        model.addAttribute("username", user.getName());
        return "manage-products";
    }

    @GetMapping("/add-products")
    public String addProducts(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        model.addAttribute("username", user.getName());
        
        if (!model.containsAttribute("productForm")) {
            model.addAttribute("productForm", new Product());
        }

        return "add-products";
    }

    @PostMapping("/product-added")
    public String addProduct(@ModelAttribute ("productForm") Product product,
                             @RequestParam("images") MultipartFile[] images,
                             RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product);

            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    imageProductService.saveProductImage(product, image);
                }
            }
            return "redirect:/product/manage-products";
        } catch (Exception e) {
           redirectAttributes.addFlashAttribute("productForm", product);
           redirectAttributes.addFlashAttribute("erro", e.getMessage());
           return "redirect:/product/add-products";
        }
    }

}
