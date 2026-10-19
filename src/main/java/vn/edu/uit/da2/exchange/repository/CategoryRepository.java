package vn.edu.uit.da2.exchange.repository;

import vn.edu.uit.da2.exchange.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository
        extends JpaRepository<Category, UUID> {

    // Kiem tra ten danh muc da ton tai hay chua
    boolean existsByNameIgnoreCase(String name);

    // Kiem tra ten danh muc trung khi cap nhat,
    // bo qua chinh danh muc dang duoc cap nhat
    boolean existsByNameIgnoreCaseAndCategoryIdNot(
            String name,
            UUID categoryId
    );
}
