package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CategoryService {
    Category findById(Long id);

    List<Category> findSubcategories(Category category);

    List<Category> findAllCategoriesInHierarchy(Category category);

    void deleteById(Long id);

    void save(Category category);

    List<Category> findAll();

    Page<Category> findAllBySpecification(Specification<Category> specification, Pageable pageable);

    List<Category> findLeafCategories();

    List<Category> findAvailableParentCategories();
}
