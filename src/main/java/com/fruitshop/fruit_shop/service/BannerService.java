package com.fruitshop.fruit_shop.service;

import com.fruitshop.fruit_shop.entity.Banner;
import com.fruitshop.fruit_shop.repository.BannerRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BannerService {

	private final BannerRepository bannerRepository;

	public BannerService(BannerRepository bannerRepository) {
		this.bannerRepository = bannerRepository;
	}

	public Page<Banner> getAll(Pageable pageable) {
		return bannerRepository.findAll(pageable);
	}

	public Page<Banner> search(String keyword, Pageable pageable) {
		return bannerRepository.findByTitleContainingIgnoreCase(keyword, pageable);
	}

	public Banner findById(Integer id) {

		return bannerRepository.findById(id).orElse(null);

	}

	public void create(Banner banner, MultipartFile file) {

		try {

			if (!file.isEmpty()) {

				String base64 = Base64.getEncoder().encodeToString(file.getBytes());

				banner.setImage("data:image/png;base64," + base64);
			}

			bannerRepository.save(banner);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// UPDATE
	public void update(Banner banner, MultipartFile file, String oldImage) {

		try {

			if (file != null && !file.isEmpty()) {

				String base64 = Base64.getEncoder().encodeToString(file.getBytes());

				banner.setImage("data:image/png;base64," + base64);

			} else {

				banner.setImage(oldImage);
			}

			bannerRepository.save(banner);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Integer id) {
		bannerRepository.deleteById(id);
	}

	public List<Banner> findByPosition() {
		return bannerRepository.findByPositionAndStatus(Banner.BannerPosition.home_slider, Banner.BannerStatus.active);
	}
}