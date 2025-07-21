package com.cybermoto.service;


import com.cybermoto.entity.Product;
import org.springframework.web.multipart.MultipartFile;

public interface ImageProductService {

    void saveProductImage(Product product, MultipartFile image, boolean isMain) throws IllegalArgumentException;
    void deleteImageById(Long id);
}
