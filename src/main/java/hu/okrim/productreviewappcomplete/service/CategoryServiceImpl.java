package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Category.class));
    }

    @Override
    public List<Category> findSubcategories(Category category) {
        return categoryRepository.findAllByParentCategoryId(category.getId())
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Category> findAllCategoriesInHierarchy(Category category) {
        List<Category> categoryHierarchy = new ArrayList<>();
        Category examinedCategory = category;
        // Add current and all higher categories to return list
        do {
            categoryHierarchy.add(examinedCategory);
            examinedCategory = examinedCategory.getParentCategory();
        } while (examinedCategory != null);
        // Add all subcategories to return list recursively
        collectCategoriesInHierarchyRecursive(category, categoryHierarchy);
        return categoryHierarchy;
    }

    private void collectCategoriesInHierarchyRecursive(Category category, List<Category> categoryHierarchy) {
        // Add the current category to the hierarchy
        categoryHierarchy.add(category);
        // Recursively collect subcategories
        List<Category> subcategories = findSubcategories(category);
        for (Category subcategory : subcategories) {
            collectCategoriesInHierarchyRecursive(subcategory, categoryHierarchy);
        }
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Page<Category> findAllBySpecification(Specification<Category> specification, Pageable pageable) {
        return categoryRepository.findAll(specification, pageable);
    }

    @Override
    public List<Category> findLeafCategories() {
        return categoryRepository.findLeafCategories();
    }

    @Override
    public List<Category> findAvailableParentCategories() {
        return categoryRepository.findAvailableParentCategories();
    }
}
