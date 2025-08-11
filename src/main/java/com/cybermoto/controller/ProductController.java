package com.cybermoto.controller;

import com.cybermoto.entity.ImageProduct;
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
import java.util.Arrays;
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
        if(principal == null) {
            return "redirect:/admin/login";
        }
        List<Product> products = productRepository.findAllWithImages();
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
    public String addProduct(@ModelAttribute("productForm") Product product,
                             @RequestParam(value = "uploadImages", required = false) MultipartFile[] images,
                             @RequestParam(value = "isMain", required = false) Boolean isMain,
                             RedirectAttributes ra) {
        try {
            boolean hasAnyFile = images != null && Arrays.stream(images).anyMatch(f -> f != null && !f.isEmpty());
            if (!hasAnyFile) {
                ra.addFlashAttribute("productForm", product);
                ra.addFlashAttribute("erro", "Envie pelo menos uma imagem.");
                return "redirect:/product/add-products";
            }

            if (isMain == null || !isMain) {
                ra.addFlashAttribute("productForm", product);
                ra.addFlashAttribute("erro", "Selecione uma imagem principal.");
                return "redirect:/product/add-products";
            }

            productService.saveProduct(product);

            boolean mainUsed = false;
            for (MultipartFile file : images) {
                if (file != null && !file.isEmpty()) {
                    imageProductService.saveProductImage(product, file, !mainUsed); // true só na primeira
                    mainUsed = true;
                }
            }

            return "redirect:/product/manage-products";
        } catch (Exception e) {
            ra.addFlashAttribute("productForm", product);
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/product/add-products";
        }
    }


    @PostMapping("/toggle-status/{id}")
    public String toggleProductStatus(@PathVariable Long id) {
        productService.toggleProductStatus(id);
        return "redirect:/product/manage-products";
    }

    @GetMapping("edit-product/{id}")
    public String editProducts(@PathVariable Long id, Model model, Principal principal) {
        if(principal == null) {
            return "redirect:/admin/login";
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException ("Produto não encontrado"));

        String username = principal.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        model.addAttribute("productData", product);
        model.addAttribute("userData", user);
        return "edit-product";
    }

    @PostMapping("/delete-image/{id}")
    public String deleteImage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
       imageProductService.deleteImageById(id);
       redirectAttributes.addFlashAttribute("msg", "imagem removida com sucesso!");
       return "redirect:/product/edit-product";
    }

    @PostMapping("update-product")
    public String updateProduct(@ModelAttribute("productData") Product product,
                                @RequestParam (value = "uploadImages", required = false) MultipartFile[] uploadImages,
                                RedirectAttributes redirectAttributes)  {
    try {
      productService.updateProduct(product);
      if(uploadImages != null) {
          for(MultipartFile image : uploadImages) {
            if(!image.isEmpty()) {
                imageProductService.saveProductImage(product, image, false);
            }
          }
      }
      redirectAttributes.addFlashAttribute("msg", "produto atualizado com sucesso!");
      return "redirect:/product/manage-products";
    }
    catch(Exception e) {
      redirectAttributes.addFlashAttribute("erro", "erro ao atualizar produto"+  e.getMessage());
      return "redirect:/prodcuct/edit-product" + product.getId();
    }
    }


}
