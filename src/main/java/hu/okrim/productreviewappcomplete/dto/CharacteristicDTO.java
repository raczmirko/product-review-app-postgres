package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacteristicDTO {
    private Long id;
    private String name;
    private String unitOfMeasureName;
    private String unitOfMeasure;
    private String description;
    private Set<Category> categories;
}
