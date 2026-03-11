package com.fruitshop.fruit_shop.repository;

import com.fruitshop.fruit_shop.entity.Favorite;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    @Query("""
        SELECT f FROM Favorite f
        WHERE LOWER(f.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(f.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Favorite> search(String keyword, Pageable pageable);
    long countByProductId(Integer productId);
    
    Favorite findByUserIdAndProductId(Integer userId, Integer productId);
    List<Favorite> findByUserIdOrderByCreatedAtDesc(Integer userId);


}