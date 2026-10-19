
package vn.edu.uit.da2.exchange.controller;

import vn.edu.uit.da2.exchange.dto.ProductRequest;
import vn.edu.uit.da2.exchange.dto.ProductResponse;
import vn.edu.uit.da2.exchange.service.ProductService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET: Lay danh sach san pham dang kha dung
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        return ResponseEntity.ok(
                productService.getAvailableProducts()
        );
    }

    // GET: Tim kiem, loc va sap xep san pham
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                productService.searchProducts(
                        keyword,
                        categoryId,
                        sortBy,
                        direction
                )
        );
    }

    // GET: Xem chi tiet san pham
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable UUID productId) {

        ProductResponse response =
                productService.getAvailableProductById(productId);

        return ResponseEntity.ok(response);
    }

    // POST: Dang san pham moi
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {

        ProductResponse response = productService.createProduct(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // PUT: Cap nhat san pham
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {

        ProductResponse response = productService.updateProduct(
                productId,
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    // DELETE: An tin dang, khong xoa vat ly
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> hideProduct(
            @PathVariable UUID productId,
            Authentication authentication) {

        productService.hideProduct(
                productId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
