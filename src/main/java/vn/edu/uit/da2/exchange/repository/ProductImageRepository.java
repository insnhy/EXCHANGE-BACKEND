package vn.edu.uit.da2.exchange.repository;

import vn.edu.uit.da2.exchange.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, UUID> {

    List<ProductImage> findByProduct_ProductId(UUID productId);
}
