package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryHierarchyDTO {
    private Category parentParentCategory;
    private Category currentParentCategory;
    private Category currentCategory;
    private List<Category> currentSubcategories;
    private HashMap<Long, List<Category>> subSubcategories;
}
