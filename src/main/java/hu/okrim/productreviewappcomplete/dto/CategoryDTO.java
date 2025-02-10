package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.model.Characteristic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Category parentCategory;
    private String description;
    private Set<Characteristic> characteristics;
}
