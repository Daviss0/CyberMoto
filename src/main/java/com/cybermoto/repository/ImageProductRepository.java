package com.cybermoto.repository;

import com.cybermoto.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository extends JpaRepository <ImageProduct, Long> {
  List<ImageProduct> findByProductId(Long productId);
}
