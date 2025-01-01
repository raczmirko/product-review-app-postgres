package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.ProductCharacteristicValueDTO;
import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;

public class ProductCharacteristicValueMapper {
    public static ProductCharacteristicValueDTO mapToProductCharacteristicValueDTO (ProductCharacteristicValue productCharacteristicValue){
        return new ProductCharacteristicValueDTO(
                productCharacteristicValue.getId(),
                productCharacteristicValue.getProduct(),
                productCharacteristicValue.getCharacteristic(),
                productCharacteristicValue.getValue()
        );
    }
    public static ProductCharacteristicValue mapToProductCharacteristicValue (ProductCharacteristicValueDTO productCharacteristicValueDTO){
        return new ProductCharacteristicValue(
                productCharacteristicValueDTO.getId() != null ? productCharacteristicValueDTO.getId() : null,
                productCharacteristicValueDTO.getProduct(),
                productCharacteristicValueDTO.getCharacteristic(),
                productCharacteristicValueDTO.getValue()
        );
    }
}
