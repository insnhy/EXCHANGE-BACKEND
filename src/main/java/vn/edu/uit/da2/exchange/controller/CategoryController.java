package vn.edu.uit.da2.exchange.controller;

import vn.edu.uit.da2.exchange.entity.Category;
import vn.edu.uit.da2.exchange.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET: Lay danh sach danh muc
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    // POST: Tao danh muc moi
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category) {

        Category created =
                categoryService.createCategory(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // PUT: Cap nhat danh muc
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable UUID categoryId,
            @RequestBody Category category) {

        return ResponseEntity.ok(
                categoryService.updateCategory(
                        categoryId, category
                )
        );
    }

    // DELETE: Xoa danh muc
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}
