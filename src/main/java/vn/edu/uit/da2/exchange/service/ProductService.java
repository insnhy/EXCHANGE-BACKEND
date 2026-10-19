
package vn.edu.uit.da2.exchange.service;

import java.util.List;
import java.util.UUID;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;

import vn.edu.uit.da2.exchange.dto.ProductRequest;
import vn.edu.uit.da2.exchange.dto.ProductResponse;
import vn.edu.uit.da2.exchange.entity.Category;
import vn.edu.uit.da2.exchange.entity.Product;
import vn.edu.uit.da2.exchange.entity.User;
import vn.edu.uit.da2.exchange.repository.CategoryRepository;
import vn.edu.uit.da2.exchange.repository.ProductRepository;
import vn.edu.uit.da2.exchange.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // POST: Tao san pham moi
    @Transactional
    public ProductResponse createProduct(
            ProductRequest request,
            String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Khong tim thay tai khoan dang nhap"
                    )
                );

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Tai khoan khong hoat dong"
            );
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Danh muc khong ton tai"
                    )
                );

        Product product = new Product();

        product.setUser(user);
        product.setCategory(category);
        product.setTitle(request.getTitle().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCondition(request.getCondition().trim());
        product.setTransactionType(
            request.getTransactionType().trim()
        );
        product.setStatus("AVAILABLE");

        Product saved = productRepository.save(product);

        return toProductResponse(saved);
    }

    // GET: Lay danh sach san pham kha dung
    @Transactional(readOnly = true)
    public List<ProductResponse> getAvailableProducts() {

        return productRepository.findByStatus("AVAILABLE")
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    // Chuyen Entity thanh DTO
    private ProductResponse toProductResponse(Product product) {

        User user = product.getUser();
        Category category = product.getCategory();

        return new ProductResponse(
            product.getProductId(),
            user.getUserId(),
            user.getUsername(),
            category.getCategoryId(),
            category.getName(),
            product.getTitle(),
            product.getDescription(),
            product.getPrice(),
            product.getCondition(),
            product.getTransactionType(),
            product.getStatus(),
            product.getImageUrl(),
            product.getCreatedAt()
        );
    }

    // GET: Xem chi tiet san pham
    @Transactional(readOnly = true)
    public ProductResponse getAvailableProductById(
            UUID productId) {

        Product product = productRepository.findById(productId)
                .filter(p ->
                    "AVAILABLE".equals(p.getStatus())
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "San pham khong ton tai hoac khong kha dung"
                    )
                );

        return toProductResponse(product);
    }

    // PUT: Cap nhat san pham
    @Transactional
    public ProductResponse updateProduct(
            UUID productId,
            ProductRequest request,
            String username) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Tai khoan khong ton tai"
                    )
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay san pham"
                    )
                );

        if (!product.getUser().getUserId()
                .equals(currentUser.getUserId())) {

            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Ban khong co quyen sua san pham nay"
            );
        }

        if (!"AVAILABLE".equals(product.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "San pham khong con kha dung de cap nhat"
            );
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Danh muc khong ton tai"
                    )
                );

        product.setTitle(request.getTitle().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCondition(request.getCondition().trim());
        product.setTransactionType(
            request.getTransactionType().trim()
        );
        product.setCategory(category);

        Product saved = productRepository.save(product);

        return toProductResponse(saved);
    }

    // DELETE: An tin dang
    @Transactional
    public void hideProduct(
            UUID productId,
            String username) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Tai khoan khong ton tai"
                    )
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay san pham"
                    )
                );

        if (!product.getUser().getUserId()
                .equals(currentUser.getUserId())) {

            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Ban khong co quyen an san pham nay"
            );
        }

        product.setStatus("HIDDEN");
        productRepository.save(product);
    }

    // GET: Tim kiem, loc va sap xep san pham
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(
            String keyword,
            UUID categoryId,
            String sortBy,
            String direction) {

        Stream<Product> stream =
                productRepository.findByStatus("AVAILABLE")
                        .stream();

        if (keyword != null && !keyword.isBlank()) {
            String key = keyword.trim().toLowerCase(Locale.ROOT);

            stream = stream.filter(product ->
                (product.getTitle() != null
                    && product.getTitle()
                        .toLowerCase(Locale.ROOT)
                        .contains(key))
                ||
                (product.getDescription() != null
                    && product.getDescription()
                        .toLowerCase(Locale.ROOT)
                        .contains(key))
            );
        }

        if (categoryId != null) {
            stream = stream.filter(product ->
                product.getCategory() != null
                    && categoryId.equals(
                        product.getCategory().getCategoryId()
                    )
            );
        }

        Comparator<Product> comparator;

        if ("price".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(
                Product::getPrice
            );
        } else if ("title".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(
                Product::getTitle,
                String.CASE_INSENSITIVE_ORDER
            );
        } else {
            comparator = Comparator.comparing(
                Product::getCreatedAt
            );
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        return stream
                .sorted(comparator)
                .map(this::toProductResponse)
                .toList();
    }
}
