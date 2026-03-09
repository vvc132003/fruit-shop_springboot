package com.fruitshop.fruit_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "banners")
@Getter
@Setter
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private String image;

    private String link;

    @Enumerated(EnumType.STRING)
    private BannerPosition position;

    @Enumerated(EnumType.STRING)
    private BannerStatus status;
    public enum BannerPosition {
        home_top,
        home_slider
    }

    public enum BannerStatus {
        active,
        inactive
    }
}