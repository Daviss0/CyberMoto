package com.cybermoto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CART_ITEMS", indexes = {
        @Index(name = "idx_cart_items_cart", columnList = "cart_id"),
        @Index(name = "idx_cart_items_product", columnList = "product_id")
})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 160)
    private String productNameSnapshot;

    @Column(nullable = true, length = 80)
    private String productBrandSnapshot;

    @Column(nullable = true, length = 255)
    private String mainImageFilename;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;


    //getters & setters
    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}

    public Cart getCart() {return cart;}

    public void setCart(Cart cart) {this.cart = cart;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}

    public String getProductNameSnapshot() {return productNameSnapshot;}

    public void setProductNameSnapshot(String productNameSnapshot) {this.productNameSnapshot = productNameSnapshot;}

    public String getProductBrandSnapshot() {return productBrandSnapshot;}

    public void setProductBrandSnapshot(String productBrandSnapshot) {this.productBrandSnapshot = productBrandSnapshot;}

    public String getMainImageFilename() {return mainImageFilename;}

    public void setMainImageFilename(String mainImageFilename) {this.mainImageFilename = mainImageFilename;}

    public Integer getQuantity() {return quantity;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public BigDecimal getUnitPrice() {return unitPrice;}

    public void setUnitPrice(BigDecimal unitPrice) {this.unitPrice = unitPrice;}

    public BigDecimal getTotalPrice() {return totalPrice;}

    public void setTotalPrice(BigDecimal totalPrice) {this.totalPrice = totalPrice;}

}
