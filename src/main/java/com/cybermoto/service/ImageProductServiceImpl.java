package com.cybermoto.service;

import com.cybermoto.entity.ImageProduct;
import com.cybermoto.entity.Product;
import com.cybermoto.repository.ImageProductRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
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
                !(contentType.equals("image/png") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/webp") ||
                        contentType.equals("image/jfif"))) {
            throw new IllegalArgumentException("Formato de imagem inválido! Permitido: PNG, JPEG, WEBP");
        }

        // 2) Validação do tamanho (5MB)
        if (image.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Imagem muito grande! Tamanho máximo 5MB");
        }

        // 3) Se for imagem principal, desmarcar a atual
        if (isMain) {
            product.getImages().forEach(img -> {
                if (img.isMain()) {
                    img.setMain(false);
                    imageProductRepository.save(img);
                }
            });
        }

        // 4) Geração de nome limpo
        String originalName = image.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalName).toLowerCase();

        if (extension.equals("jfif")) {
            extension = "jpg";
        }

        // Remoção de acentos e caracteres inválidos
        String sanitized = Normalizer
                .normalize(originalName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")  // Remove acentos
                .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");  // Substitui espaços e símbolos

        String fileName = UUID.randomUUID().toString() + "-" + sanitized;

        // 5) Salvamento físico no diretório correto
        Path directory = Paths.get("uploads/products/");
        Path path = directory.resolve(fileName);

        try {
            Files.createDirectories(directory);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 6) Registro da imagem no banco
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

