package com.cybermoto.service;

import com.cybermoto.entity.Product;

public interface ProductService {

    void saveProduct(Product product) throws IllegalArgumentException;
}
