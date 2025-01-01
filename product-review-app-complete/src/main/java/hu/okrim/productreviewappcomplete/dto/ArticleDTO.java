package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private String name;
    private Brand brand;
    private Category category;
    private String description;
}
