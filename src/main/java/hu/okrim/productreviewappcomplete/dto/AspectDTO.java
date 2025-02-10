package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AspectDTO {
    private Long id;
    private String name;
    private String question;
    private Category category;
}
