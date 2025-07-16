package com.cybermoto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
public class Product {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCTID")
    private Long id;

    @Column(name = "PRODUCTNAME", length = 200, nullable = false)
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 3, max = 200, message = "O nome deve ter entre 3 e 200 caracteres")
    private String productName;

    @Column(name = "BRAND", nullable = false)
    @NotBlank(message = "A marca é obrigatória")
    @Size(min = 3, max = 100, message = "A marca deve ter entre 3 e 100 caracteres.")
    private String brand;

    @Column(name = "RATING", precision = 2, scale = 1)
    @DecimalMin(value = "0.5", message = "A avaliação minima é 0.5.")
    @DecimalMax(value = "5.0", message = "A avaliação maxima é 5.0.")
    @Builder.Default
    private BigDecimal rating = BigDecimal.valueOf(0.5);

    @Column(name = "DESCRIPTION", nullable = false, length = 2000)
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 2000, message = "A descrição deve ter entre 10 e 2000 caracteres.")
    private String description;

    @Column (name = "PRICE", nullable = false)
    @DecimalMin(value = "0.01", message = "O valor minimo é R$ 0,1")
    private BigDecimal price;

    @Column (name = "QUANTITY", nullable = false)
    @Min(value = 0, message = "A quantidade no estoque não pode ser negativa")
    private int quantity;

    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageProduct> images = new ArrayList<>();


    // getters e setters
    public void setImages(List<ImageProduct> images) {this.images = images;}

    public List<ImageProduct> getImages() {return images;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public void setPrice(BigDecimal price) {this.price = price;}

    public BigDecimal getPrice() {return price;}

    public void setDescription(String description) {this.description = description;}

    public String getDescription() {return description;}

    public String getBrand() {return brand;}

    public void setBrand(String brand) {this.brand = brand;}

    public BigDecimal getRating() {return rating;}

    public void setRating(BigDecimal rating) {this.rating = rating;}

    public void setProductName(String productName) {this.productName = productName;}

    public String getProductName() {return productName;}

    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}
}
