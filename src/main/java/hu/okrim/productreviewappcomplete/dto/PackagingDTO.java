package hu.okrim.productreviewappcomplete.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackagingDTO {
    private Long id;
    private String name;
    private Short amount;
    private String unitOfMeasureName;
    private String unitOfMeasure;
    private String size;
}
