package com.fruitshop.fruit_shop.controllerAPI;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.fruitshop.fruit_shop.dto.request.ProductRequest;
import com.fruitshop.fruit_shop.dto.response.ApiResponse;
import com.fruitshop.fruit_shop.dto.response.Meta;
import com.fruitshop.fruit_shop.dto.response.ProductResponse;
import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.entity.Product;
import com.fruitshop.fruit_shop.mapper.ProductMapper;
import com.fruitshop.fruit_shop.service.CategoryService;
import com.fruitshop.fruit_shop.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductApiController(ProductService productService,
                                CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // =========================
    // GET LIST PRODUCT
    // =========================
    @GetMapping
    public ApiResponse<List<ProductResponse>> list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "keyword", required = false) String keyword) {

        int size = 10;

        Page<Product> pageData;

        if (keyword != null && !keyword.isEmpty()) {
            pageData = productService.search(keyword, PageRequest.of(page - 1, size));
        } else {
            pageData = productService.getAll(PageRequest.of(page - 1, size));
        }

        List<ProductResponse> items = pageData.getContent()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();

        Meta meta = new Meta(
                page,
                size,
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                items.size()
        );

        return ApiResponse.success(
                "Lấy danh sách sản phẩm thành công",
                items,
                meta
        );
    }

    // =========================
    // GET PRODUCT BY ID
    // =========================
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable("id") Integer id) {
        Product product = productService.findById(id);
        if (product == null) {
            return ApiResponse.error("Không tìm thấy sản phẩm");
        }

        ProductResponse response = ProductMapper.toResponse(product);

        return ApiResponse.success("Lấy sản phẩm thành công", response);
    }

    // =========================
    // CREATE PRODUCT
    // =========================
    @PostMapping
    public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {

        Category category = categoryService.findById(request.getCategoryId());

        Product product = ProductMapper.toEntity(request, category);

        productService.save(product);

        ProductResponse response = ProductMapper.toResponse(product);

        return ApiResponse.success("Thêm sản phẩm thành công", response);
    }

    // =========================
    // UPDATE PRODUCT
    // =========================
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(
            @PathVariable("id") Integer id,
            @RequestBody ProductRequest request) {

        Product oldProduct = productService.findById(id);

        if (oldProduct == null) {
            return ApiResponse.error("Không tìm thấy sản phẩm");
        }

        Category category = categoryService.findById(request.getCategoryId());

        Product product = ProductMapper.toEntity(request, category);
        product.setId(id);

        productService.save(product);

        ProductResponse response = ProductMapper.toResponse(product);

        return ApiResponse.success("Cập nhật sản phẩm thành công", response);
    }

    // =========================
    // DELETE PRODUCT
    // =========================
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Integer id) {

        productService.delete(id);

        return ApiResponse.success("Xoá sản phẩm thành công", null);
    }

}