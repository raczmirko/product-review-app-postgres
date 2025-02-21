package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CategoryDTO;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.service.CategoryService;
import hu.okrim.productreviewappcomplete.specification.CategorySpecificationBuilder;
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
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.findById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.categoryDeleteErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deleteCategories(@PathVariable("ids") Long[] ids) {
        for (Long id : ids) {
            try {
                categoryService.deleteById(id);
            } catch (Exception ex) {
                String errorMessage = SqlExceptionMessageHandler.categoryDeleteErrorMessage(ex);
                return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> modifyCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setParentCategory(categoryDTO.getParentCategory());
        existingCategory.setCharacteristics(categoryDTO.getCharacteristics());
        try {
            categoryService.save(existingCategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String message = SqlExceptionMessageHandler.categoryUpdateErrorMessage(ex);
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription());
        if (categoryDTO.getParentCategory() != null) category.setParentCategory(categoryDTO.getParentCategory());
        try {
            categoryService.save(category);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            String message = SqlExceptionMessageHandler.categoryCreateErrorMessage(ex);
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Category>> searchCategories(@RequestParam(value = "searchText", required = false) String searchText,
                                                           @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                           @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam("pageNumber") Integer pageNumber,
                                                           @RequestParam("orderByColumn") String orderByColumn,
                                                           @RequestParam("orderByDirection") String orderByDirection) {

        CategorySpecificationBuilder<Category> categoryCategorySpecificationBuilder = new CategorySpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> categoryCategorySpecificationBuilder.withId(searchText);
                case "name" -> categoryCategorySpecificationBuilder.withName(searchText);
                case "description" -> categoryCategorySpecificationBuilder.withDescription(searchText);
                case "parentCategory" -> categoryCategorySpecificationBuilder.withParentCategory(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                categoryCategorySpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Category> specification = categoryCategorySpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Category> categoryPage = categoryService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @GetMapping("/leaf-categories")
    public ResponseEntity<List<Category>> getLeafCategories() {
        List<Category> categories = categoryService.findLeafCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/available-parent-categories")
    public ResponseEntity<List<Category>> getAvailableParentCategories() {
        List<Category> categories = categoryService.findAvailableParentCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
