package com.cybermoto.service;

import com.cybermoto.entity.ImageProduct;
import com.cybermoto.entity.Product;
import com.cybermoto.repository.ImageProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageProductServiceImpl implements ImageProductService{

    @Autowired
    private ImageProductRepository imageProductRepository;

    @Override
    public void saveProductImage(Product product, MultipartFile image, boolean isMain) {

        // 1) Validação do tipo
        String contentType = image.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/png") || contentType.equals("image/jpeg") || contentType.equals("image/webp"))) {
            throw new IllegalArgumentException("Formato de imagem inválido! Permitido: PNG, JPEG, WEBP");
        }

        // 2) Validação do tamanho (5MB)
        if (image.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Imagem muito grande! Tamanho máximo 5MB");
        }
        //3 salvamento da imagem principal
        if(isMain) {
            product.getImages().forEach(img -> {
                if(img.isMain()) {
                    img.setMain(false);
                    imageProductRepository.save(img);
                }
            });
        }


        // 4) Salvamento físico + banco
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        Path diretorio = Paths.get("uploads/products/" + product.getId() + "/");
        Path path = diretorio.resolve(fileName);

        try {
            Files.createDirectories(diretorio);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            ImageProduct img = new ImageProduct();
            img.setFilename(fileName);
            img.setProduct(product);
            img.setMain(isMain);
            imageProductRepository.save(img);


        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem", e);
        }
    }



    @Override
    public void deleteImageById(Long id) {
        ImageProduct img = imageProductRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Imagem não encontrada"));

        Path path = Paths.get("uploads/products/" + img.getProduct().getId() + "/" + img.getFilename());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir imagem", e);
        }

        imageProductRepository.delete(img);
    }



}

