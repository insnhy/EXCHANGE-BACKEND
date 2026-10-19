package vn.edu.uit.da2.exchange.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductRequest {

    @NotBlank(message = "Tieu de khong duoc de trong")
    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull(message = "Gia san pham khong duoc de trong")
    @DecimalMin(value = "0.0", message = "Gia khong duoc am")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotBlank(message = "Tinh trang san pham khong duoc de trong")
    @Size(max = 50)
    private String condition;

    @NotBlank(message = "Loai giao dich khong duoc de trong")
    @Size(max = 30)
    private String transactionType;

    @NotNull(message = "Vui long chon danh muc")
    private UUID categoryId;

    public ProductRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
