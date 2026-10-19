package vn.edu.uit.da2.exchange.controller;

import vn.edu.uit.da2.exchange.dto.ProductImageResponse;
import vn.edu.uit.da2.exchange.service.ProductImageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(
            ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping(
            value = "/{productId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<ProductImageResponse>> uploadImages(
            @PathVariable UUID productId,
            @RequestParam("files") List<MultipartFile> files,
            Authentication authentication) {

        List<ProductImageResponse> result =
                productImageService.uploadImages(
                        productId,
                        files,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getProductImages(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                productImageService.getProductImages(productId)
        );
    }
}
