package com.cybermoto.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CARTS", indexes = {@Index(name = "idx_carts_client", columnList = "client_id")})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "client_id", unique = true, nullable = false)
    private Client client;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal =  BigDecimal.ZERO;

    @Column (nullable = false, precision = 19, scale = 2)
    private BigDecimal freight = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19,scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void addItem(CartItem i) {
        i.setCart(this);
        this.items.add(i);
    }
    public void removeItem(CartItem i) {
        this.items.remove(i);
        i.setCart(null);
    }

    //getters & setters
    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}

    public Client getClient() {return client;}

    public void setClient(Client client) {this.client = client;}

    public List<CartItem> getItems() {return items;}

    public void setItems(List<CartItem> items) {this.items = items;}

    public BigDecimal getSubtotal() {return subtotal;}

    public void setSubtotal(BigDecimal subtotal) {this.subtotal = subtotal;}

    public BigDecimal getFreight() {return freight;}

    public void setFreight(BigDecimal freight) {this.freight = freight;}

    public BigDecimal getDiscount() {return discount;}

    public void setDiscount(BigDecimal discount) {this.discount = discount;}

    public BigDecimal getTotal() {return total;}

    public void setTotal(BigDecimal total) {this.total = total;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}

    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}


}

