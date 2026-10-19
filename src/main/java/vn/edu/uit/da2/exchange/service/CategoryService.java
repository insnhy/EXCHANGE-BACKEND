package vn.edu.uit.da2.exchange.service;

import vn.edu.uit.da2.exchange.entity.Category;
import vn.edu.uit.da2.exchange.repository.CategoryRepository;
import vn.edu.uit.da2.exchange.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository) {

        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // GET: Lay danh sach danh muc
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // POST: Tao danh muc moi
    @Transactional
    public Category createCategory(Category category) {

        String name = validateName(category.getName());

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ten danh muc da ton tai"
            );
        }

        category.setName(name);
        trimDescription(category);

        return categoryRepository.save(category);
    }

    // PUT: Cap nhat danh muc
    @Transactional
    public Category updateCategory(
            UUID categoryId,
            Category request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay danh muc"
                ));

        String name = validateName(request.getName());

        if (categoryRepository
                .existsByNameIgnoreCaseAndCategoryIdNot(
                        name, categoryId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ten danh muc da duoc su dung"
            );
        }

        category.setName(name);
        category.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        return categoryRepository.save(category);
    }

    // DELETE: Xoa danh muc neu chua co san pham su dung
    @Transactional
    public void deleteCategory(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay danh muc"
                ));

        if (productRepository
                .existsByCategory_CategoryId(categoryId)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Khong the xoa danh muc dang duoc san pham su dung"
            );
        }

        categoryRepository.delete(category);
    }

    private String validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ten danh muc khong duoc de trong"
            );
        }

        return name.trim();
    }

    private void trimDescription(Category category) {
        if (category.getDescription() != null) {
            category.setDescription(
                    category.getDescription().trim()
            );
        }
    }
}
