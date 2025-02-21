package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ProductImage;
import hu.okrim.productreviewappcomplete.service.ProductImageService;
import hu.okrim.productreviewappcomplete.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product-image")
public class ProductImageController {
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;

    @PostMapping(path = "{productId}/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createProductImage(@PathVariable("productId") Long productId,
                                                @RequestParam MultipartFile[] files) {
        Product product = productService.findById(productId);

        if (files != null && files.length != 0) {
            try {
                for (MultipartFile file : files) {
                    byte[] imageBytes = file.getBytes();
                    productImageService.save(new ProductImage(product, imageBytes));
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ex) {
                String message = "Failed to upload images: " + ex.getMessage();
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            String message = "Product with ID " + productId + " not found or images are missing.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteProductImage(@PathVariable("id") Long id) {
        try {
            productImageService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            //TODO create custom exceptions
            String errorMessage = ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }
}
