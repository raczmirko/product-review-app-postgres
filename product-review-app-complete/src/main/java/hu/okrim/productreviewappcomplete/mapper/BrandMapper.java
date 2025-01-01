package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.BrandDTO;
import hu.okrim.productreviewappcomplete.model.Brand;

public class BrandMapper {
    public static BrandDTO mapToBrandDTO (Brand brand){
        return new BrandDTO(
                brand.getId(),
                brand.getName(),
                brand.getCountryOfOrigin(),
                brand.getDescription()
        );
    }
    public static Brand mapToBrand (BrandDTO brandDTO){
        return new Brand(
                brandDTO.getId() != null ? brandDTO.getId() : null,
                brandDTO.getName(),
                brandDTO.getCountryOfOrigin(),
                brandDTO.getDescription()
        );
    }
}
