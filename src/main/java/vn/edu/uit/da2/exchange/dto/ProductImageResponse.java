package vn.edu.uit.da2.exchange.dto;

import java.util.UUID;

public class ProductImageResponse {

    private final UUID imageId;
    private final UUID productId;
    private final String imageUrl;

    public ProductImageResponse(
            UUID imageId,
            UUID productId,
            String imageUrl) {
        this.imageId = imageId;
        this.productId = productId;
        this.imageUrl = imageUrl;
    }

    public UUID getImageId() {
        return imageId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
