package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.PackagingDTO;
import hu.okrim.productreviewappcomplete.model.Packaging;

public class PackagingMapper {
    public static PackagingDTO mapToPackagingDTO(Packaging packaging) {
        return new PackagingDTO(
                packaging.getId(),
                packaging.getName(),
                packaging.getAmount(),
                packaging.getUnitOfMeasure(),
                packaging.getUnitOfMeasureName(),
                packaging.getSize()
        );
    }

    public static Packaging mapToPackaging(PackagingDTO packagingDTO) {
        return new Packaging(
                packagingDTO.getId() != null ? packagingDTO.getId() : null,
                packagingDTO.getName(),
                packagingDTO.getAmount(),
                packagingDTO.getUnitOfMeasure(),
                packagingDTO.getUnitOfMeasureName(),
                packagingDTO.getSize()
        );
    }
}
