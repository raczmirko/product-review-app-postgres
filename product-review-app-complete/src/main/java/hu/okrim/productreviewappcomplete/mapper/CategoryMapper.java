package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.CategoryDTO;
import hu.okrim.productreviewappcomplete.model.Category;

public class CategoryMapper {
    public static CategoryDTO mapToCategoryDTO (Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getParentCategory(),
                category.getDescription(),
                category.getCharacteristics()
        );
    }
    public static Category mapToCategory (CategoryDTO categoryDTO){
        return new Category(
                categoryDTO.getId() != null ? categoryDTO.getId() : null,
                categoryDTO.getName(),
                categoryDTO.getParentCategory(),
                categoryDTO.getDescription()
        );
    }
}
