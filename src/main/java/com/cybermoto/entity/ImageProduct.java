package com.cybermoto.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_IMAGES")
public class ImageProduct {

    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "FILENAME", nullable = false)
    private String filename;

    @Column (name = "DATECREATED", nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "ISMAIN", nullable = false)
    private boolean isMain = false;


    //getters e setters
    public boolean isMain() {return isMain;}

    public void setMain(boolean main) {isMain = main;}

    public void setDateCreated(LocalDateTime dateCreated) {this.dateCreated = dateCreated;}

    public LocalDateTime getDateCreated() {return dateCreated;}

    public void setFilename(String filename) {this.filename = filename;}

    public String getFilename() {return filename;}

    public void setProduct(Product product) {this.product = product;}

    public Product getProduct() {return product;}

    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;}
}
