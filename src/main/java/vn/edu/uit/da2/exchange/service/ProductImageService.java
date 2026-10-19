package vn.edu.uit.da2.exchange.service;

import vn.edu.uit.da2.exchange.dto.ProductImageResponse;
import vn.edu.uit.da2.exchange.entity.Product;
import vn.edu.uit.da2.exchange.entity.ProductImage;
import vn.edu.uit.da2.exchange.repository.ProductImageRepository;
import vn.edu.uit.da2.exchange.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final int MAX_FILES_PER_REQUEST = 5;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final Path uploadRoot;

    public ProductImageService(
            ProductRepository productRepository,
            ProductImageRepository productImageRepository,
            @Value("${app.upload.dir:uploads}") String uploadDir) {

        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.uploadRoot = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    @Transactional
    public List<ProductImageResponse> uploadImages(
            UUID productId,
            List<MultipartFile> files,
            String username) {

        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vui long chon it nhat mot anh"
            );
        }

        if (files.size() > MAX_FILES_PER_REQUEST) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Moi lan chi duoc tai toi da 5 anh"
            );
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay san pham"
                ));

        if (product.getUser() == null
                || !username.equals(product.getUser().getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Ban khong co quyen tai anh cho san pham nay"
            );
        }

        if (!"AVAILABLE".equals(product.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Chi duoc tai anh cho san pham dang kha dung"
            );
        }

        // Kiem tra tat ca file truoc khi luu
        List<String> extensions = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "File anh khong duoc de trong"
                );
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Moi anh khong duoc vuot qua 5 MB"
                );
            }

            try {
                extensions.add(detectImageExtension(file));
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Khong doc duoc file anh"
                );
            }
        }

        Path productDirectory = uploadRoot.resolve("products")
                .normalize();

        try {
            Files.createDirectories(productDirectory);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Khong tao duoc thu muc luu anh"
            );
        }

        List<ProductImageResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            String filename = UUID.randomUUID()
                    + extensions.get(i);

            Path destination = productDirectory.resolve(filename)
                    .normalize();

            if (!destination.startsWith(productDirectory)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duong dan file khong hop le"
                );
            }

            try (InputStream input = file.getInputStream()) {
                Files.copy(
                        input,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Khong the luu file anh"
                );
            }

            String imageUrl = "/uploads/products/" + filename;

            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(imageUrl);

            ProductImage savedImage =
                    productImageRepository.save(image);

            // Luu anh dau tien vao truong image_url cua products
            // de tuong thich voi ProductResponse hien tai.
            if (product.getImageUrl() == null
                    || product.getImageUrl().isBlank()) {
                product.setImageUrl(imageUrl);
                productRepository.save(product);
            }

            responses.add(new ProductImageResponse(
                    savedImage.getImageId(),
                    product.getProductId(),
                    savedImage.getImageUrl()
            ));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public List<ProductImageResponse> getProductImages(UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Khong tim thay san pham"
            );
        }

        return productImageRepository
                .findByProduct_ProductId(productId)
                .stream()
                .map(image -> new ProductImageResponse(
                        image.getImageId(),
                        productId,
                        image.getImageUrl()
                ))
                .toList();
    }

    private String detectImageExtension(MultipartFile file)
            throws IOException {

        byte[] header;

        try (InputStream input = file.getInputStream()) {
            header = input.readNBytes(12);
        }

        // JPEG: FF D8 FF
        if (header.length >= 3
                && (header[0] & 0xFF) == 0xFF
                && (header[1] & 0xFF) == 0xD8
                && (header[2] & 0xFF) == 0xFF) {
            return ".jpg";
        }

        // PNG: 89 50 4E 47 0D 0A 1A 0A
        if (header.length >= 8
                && (header[0] & 0xFF) == 0x89
                && header[1] == 0x50
                && header[2] == 0x4E
                && header[3] == 0x47
                && header[4] == 0x0D
                && header[5] == 0x0A
                && header[6] == 0x1A
                && header[7] == 0x0A) {
            return ".png";
        }

        // WEBP: RIFF....WEBP
        if (header.length >= 12
                && header[0] == 'R'
                && header[1] == 'I'
                && header[2] == 'F'
                && header[3] == 'F'
                && header[8] == 'W'
                && header[9] == 'E'
                && header[10] == 'B'
                && header[11] == 'P') {
            return ".webp";
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Chi chap nhan anh JPEG, PNG hoac WEBP"
        );
    }
}
