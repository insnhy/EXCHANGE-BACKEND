package vn.edu.uit.da2.exchange.repository;

import vn.edu.uit.da2.exchange.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    // Tim san pham theo trang thai
    List<Product> findByStatus(String status);

    // Tim san pham theo danh muc
    List<Product> findByCategory_CategoryId(UUID categoryId);

    // Tim san pham theo nguoi dang
    List<Product> findByUser_UserId(UUID userId);

    // Kiem tra danh muc co san pham tham chieu hay khong
    boolean existsByCategory_CategoryId(UUID categoryId);
}
