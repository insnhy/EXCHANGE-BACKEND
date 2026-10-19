
package vn.edu.uit.da2.exchange.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductResponse {

    private final UUID productId;
    private final UUID userId;
    private final String username;
    private final UUID categoryId;
    private final String categoryName;
    private final String title;
    private final String description;
    private final BigDecimal price;
    private final String condition;
    private final String transactionType;
    private final String status;
    private final String imageUrl;
    private final LocalDateTime createdAt;

    public ProductResponse(
            UUID productId,
            UUID userId,
            String username,
            UUID categoryId,
            String categoryName,
            String title,
            String description,
            BigDecimal price,
            String condition,
            String transactionType,
            String status,
            String imageUrl,
            LocalDateTime createdAt) {

        this.productId = productId;
        this.userId = userId;
        this.username = username;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.transactionType = transactionType;
        this.status = status;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
