package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.ProductDTO;
import hu.okrim.productreviewappcomplete.mapper.ProductMapper;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.service.ProductService;
import hu.okrim.productreviewappcomplete.specification.ProductSpecificationBuilder;
import hu.okrim.productreviewappcomplete.util.SqlExceptionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.findAll();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Product article = productService.findById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.productDeleteErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deleteProducts(@PathVariable("ids") Long[] ids) {
        for (Long id : ids) {
            try {
                productService.deleteById(id);
            } catch (Exception ex) {
                String errorMessage = SqlExceptionMessageHandler.productDeleteErrorMessage(ex);
                return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> modifyProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
        Product existingProduct = productService.findById(id);

        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            existingProduct.setArticle(productDTO.getArticle());
            existingProduct.setPackaging(productDTO.getPackaging());
            productService.save(existingProduct);
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = ProductMapper.mapToProduct(productDTO);
        try {
            productService.save(product);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam(value = "searchText", required = false) String searchText,
                                                        @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                        @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("pageNumber") Integer pageNumber,
                                                        @RequestParam("orderByColumn") String orderByColumn,
                                                        @RequestParam("orderByDirection") String orderByDirection
    ) {
        ProductSpecificationBuilder<Product> productSpecificationBuilder = new ProductSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> productSpecificationBuilder.withId(searchText);
                case "article" -> productSpecificationBuilder.withArticleName(searchText);
                case "packaging" -> productSpecificationBuilder.withPackagingName(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                productSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Product> specification = productSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Product> aspectsPage = productService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(aspectsPage, HttpStatus.OK);
    }
}
