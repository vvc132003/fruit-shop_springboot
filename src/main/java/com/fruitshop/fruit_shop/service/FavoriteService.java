package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Favorite;
import com.fruitshop.fruit_shop.repository.FavoriteRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Page<Favorite> getAll(Pageable pageable) {
        return favoriteRepository.findAll(pageable);
    }

    public Page<Favorite> search(String keyword, Pageable pageable) {
        return favoriteRepository.search(keyword, pageable);
    }
    
    public long countByProduct(Integer productId){
        return favoriteRepository.countByProductId(productId);
    }

}