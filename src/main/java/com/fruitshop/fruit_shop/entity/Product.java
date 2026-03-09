package com.fruitshop.fruit_shop.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Double price;

    private String description;

    private String image;

    private String unit;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product")
    private List<Comment> comments;
    public enum ProductStatus {
        active,
        inactive
    }
}
