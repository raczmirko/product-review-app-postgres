package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CategoryHierarchyDTO;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/category-hierarchy")
public class CategoryHierarchyController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryHierarchyDTO> getCategoryTree(@PathVariable("id") Long id) {
        // Get current category object by ID
        Category currentCategory = categoryService.findById(id);
        // Get currentParent and parentParent categories (if they exist)
        Category currentParentCategory = currentCategory.getParentCategory();
        Category parentParentCategory = currentParentCategory != null ? currentParentCategory.getParentCategory() : null;
        // Get all subcategories of current category
        List<Category> currentSubcategories = categoryService.findSubcategories(currentCategory);
        // Iterate through each subcategory and get all further subcategories of each individual subcategory
        // Each tree branch is saved in a map identified by the currentSubcategory ID and has a list of child elements assigned
        HashMap<Long, List<Category>> subSubcategories = new HashMap<>();
        for (Category category : currentSubcategories) {
            // For each subcategory found create a list and save all subSubcategories of given subcategory
            List<Category> currentBranchSubcategories = categoryService.findSubcategories(category);
            subSubcategories.put(category.getId(), currentBranchSubcategories);
        }
        // Create return object
        CategoryHierarchyDTO categoryHierarchyDTO = new CategoryHierarchyDTO();
        categoryHierarchyDTO.setCurrentCategory(currentCategory);
        categoryHierarchyDTO.setCurrentParentCategory(currentParentCategory);
        categoryHierarchyDTO.setParentParentCategory(parentParentCategory);
        categoryHierarchyDTO.setCurrentSubcategories(currentSubcategories);
        categoryHierarchyDTO.setSubSubcategories(subSubcategories);
        return new ResponseEntity<>(categoryHierarchyDTO, HttpStatus.OK);
    }
}
