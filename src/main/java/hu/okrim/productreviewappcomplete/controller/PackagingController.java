package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.PackagingDTO;
import hu.okrim.productreviewappcomplete.mapper.PackagingMapper;
import hu.okrim.productreviewappcomplete.model.Packaging;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.service.PackagingService;
import hu.okrim.productreviewappcomplete.service.ProductService;
import hu.okrim.productreviewappcomplete.specification.PackagingSpecificationBuilder;
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
@RequestMapping("/api/packaging")
public class PackagingController {
    @Autowired
    PackagingService packagingService;
    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Packaging>> getPackagings() {
        List<Packaging> packagings = packagingService.findAll();
        if (packagings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(packagings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Packaging> getPackaging(@PathVariable("id") Long id) {
        Packaging characteristic = packagingService.findById(id);
        return new ResponseEntity<>(characteristic, HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deletePackaging(@PathVariable("id") Long id) {
        try {
            packagingService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.packagingDeleteErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deletePackagings(@PathVariable("ids") Long[] ids) {
        for (Long id : ids) {
            try {
                packagingService.deleteById(id);
            } catch (Exception ex) {
                String errorMessage = SqlExceptionMessageHandler.packagingDeleteErrorMessage(ex);
                return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> modifyPackaging(@PathVariable("id") Long id, @RequestBody PackagingDTO packagingDTO) {
        Packaging existingPackaging = packagingService.findById(id);

        if (existingPackaging == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            existingPackaging.setName(packagingDTO.getName());
            existingPackaging.setAmount(packagingDTO.getAmount());
            existingPackaging.setSize(packagingDTO.getSize());
            existingPackaging.setUnitOfMeasure(packagingDTO.getUnitOfMeasure());
            existingPackaging.setUnitOfMeasureName(packagingDTO.getUnitOfMeasureName());
            packagingService.save(existingPackaging);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.packagingUpdateErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPackaging(@RequestBody PackagingDTO packagingDTO) {
        Packaging packaging = PackagingMapper.mapToPackaging(packagingDTO);
        try {
            packagingService.save(packaging);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Packaging>> searchPackagings(@RequestParam(value = "searchText", required = false) String searchText,
                                                            @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                            @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                            @RequestParam("pageSize") Integer pageSize,
                                                            @RequestParam("pageNumber") Integer pageNumber,
                                                            @RequestParam("orderByColumn") String orderByColumn,
                                                            @RequestParam("orderByDirection") String orderByDirection) {

        PackagingSpecificationBuilder<Packaging> packagingSpecificationBuilder = new PackagingSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> packagingSpecificationBuilder.withId(searchText);
                case "name" -> packagingSpecificationBuilder.withName(searchText);
                case "unitOfMeasure" -> packagingSpecificationBuilder.withUnitOfMeasure(searchText);
                case "unitOfMeasureName" -> packagingSpecificationBuilder.withUnitOfMeasureName(searchText);
                case "size" -> packagingSpecificationBuilder.withSize(searchText);
                case "amount" -> packagingSpecificationBuilder.withAmount(Short.valueOf(searchText));
                case "description" -> packagingSpecificationBuilder.withDescription(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                packagingSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Packaging> specification = packagingSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Packaging> packagingPage = packagingService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(packagingPage, HttpStatus.OK);
    }

    // Get all packagings options that are not yet assigned to an article (in form of a product)
    @GetMapping("{articleId}/available-options")
    public ResponseEntity<List<Packaging>> getAvailablePackagings(@PathVariable("articleId") Long articleId) {
        List<Packaging> allPackagings = packagingService.findAll();
        List<Product> productsOfArticle = productService.findProductsByArticleId(articleId);
        for (Product p : productsOfArticle) {
            allPackagings.remove(p.getPackaging());
        }
        if (allPackagings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allPackagings, HttpStatus.OK);
    }
}
