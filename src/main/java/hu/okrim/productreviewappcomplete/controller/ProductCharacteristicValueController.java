package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.ProductCharacteristicValueDTO;
import hu.okrim.productreviewappcomplete.mapper.ProductCharacteristicValueMapper;
import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import hu.okrim.productreviewappcomplete.service.ProductCharacteristicsValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product-characteristic-value")
public class ProductCharacteristicValueController {
    @Autowired
    ProductCharacteristicsValueService pcvService;
    @PostMapping("/create")
    public ResponseEntity<?> createOrModifyProductCharacteristicValue(@RequestBody ProductCharacteristicValueDTO pcvDTO){
        ProductCharacteristicValue pcv = ProductCharacteristicValueMapper.mapToProductCharacteristicValue(pcvDTO);
        ProductCharacteristicValue updatedPcv = pcvService.findByProductAndCharacteristic(pcv.getProduct(), pcv.getCharacteristic());
        try {
            if(updatedPcv != null){
                updatedPcv.setValue(pcv.getValue());
                pcvService.save(updatedPcv);
            }
            else {
                pcvService.save(pcv);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}/all")
    public ResponseEntity<?> findProductCharacteristicValuesByProduct(@PathVariable("id") Long id){
        List<ProductCharacteristicValue> pcvs = pcvService.findByProductId(id);
        return new ResponseEntity<>(pcvs, HttpStatus.OK);
    }
}
