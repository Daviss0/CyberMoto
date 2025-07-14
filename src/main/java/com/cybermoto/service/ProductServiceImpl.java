package com.cybermoto.service;

import com.cybermoto.entity.Product;
import com.cybermoto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Override
    public void saveProduct(Product product) throws IllegalArgumentException{

        if(product.getProductName() == null || product.getProductName().isBlank()){
            throw new IllegalArgumentException("Nome do produto é obrigatorio");
        }
        if(product.getBrand() == null || product.getBrand().isBlank()) {
            throw new IllegalArgumentException("Marca é obrigatoria");
        }
        if(product.getRating() == null) {
            throw new IllegalArgumentException("Avaliação é obrigatoria");
        }

        BigDecimal min = BigDecimal.valueOf(0.5);
        BigDecimal max = BigDecimal.valueOf(5.0);
        if(product.getRating().compareTo(min) < 0 || product.getRating().compareTo(max) > 0) {
            throw new IllegalArgumentException ("A avaliação tem que ser entre 0.5 e 5.0");
        }

        if(product.getDescription() == null || product.getDescription().isBlank()) {
            throw new IllegalArgumentException ("Descrição é obrigatoria");
        }
        if(product.getDescription().length() < 10 || product.getDescription().length() > 2000) {
            throw new IllegalArgumentException ("A descrição tem que estar entre 10 e 2000 caracteres");
        }

        if(product.getPrice() == null) {
            throw new IllegalArgumentException ("Valor é obrigatorio");
        }
        if(product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException ("Valor tem que ser maior que 0");
        }

        if(product.getQuantity() < 0) {
            throw new IllegalArgumentException ("Quantidade é obrigatoria");
        }
        productRepository.save(product);
    }

}
