package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Banner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Integer> {

	Page<Banner> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

	List<Banner> findByPositionAndStatus(Banner.BannerPosition position, Banner.BannerStatus status);
}