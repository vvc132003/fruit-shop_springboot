package com.fruitshop.fruit_shop.controller;

import com.fruitshop.fruit_shop.entity.Banner;
import com.fruitshop.fruit_shop.service.BannerService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/banners")
public class BannerController {

	private final BannerService bannerService;

	public BannerController(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	@GetMapping("")
	public String list(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		int pageSize = 10;

		PageRequest pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());

		Page<Banner> pageData;

		if (keyword != null && !keyword.isEmpty()) {
			pageData = bannerService.search(keyword, pageable);
		} else {
			pageData = bannerService.getAll(pageable);
		}

		model.addAttribute("banners", pageData.getContent());
		model.addAttribute("page", page);
		model.addAttribute("totalPages", pageData.getTotalPages());
		model.addAttribute("count", pageData.getTotalElements());
		model.addAttribute("keyword", keyword);

		return "admin/banners/list";
	}

	@GetMapping("/create")
	public String create(Model model) {

		model.addAttribute("banner", new Banner());

		return "admin/banners/create";
	}

	@PostMapping("/store")
	public String store(@ModelAttribute Banner banner, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		bannerService.create(banner, file);
		redirectAttributes.addFlashAttribute("success", "Thêm thành công!");
		return "redirect:/banners";
	}

	@GetMapping("/update")
	public String edit(@RequestParam("id") Integer id, Model model) {

		Banner banner = bannerService.findById(id);

		model.addAttribute("banner", banner);

		return "admin/banners/update";
	}

	@PostMapping("/edit")
	public String update(@ModelAttribute Banner banner,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "old_image", required = false) String oldImage, RedirectAttributes redirectAttributes) {

		bannerService.update(banner, file, oldImage);
		redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		return "redirect:/banners";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		bannerService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Xoá thành công!");
		return "redirect:/banners"; // Redirect to category list after deletion
	}
}