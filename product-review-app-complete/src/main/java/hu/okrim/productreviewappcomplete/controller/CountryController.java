package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CountryDTO;
import hu.okrim.productreviewappcomplete.mapper.CountryMapper;
import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.service.CountryService;
import hu.okrim.productreviewappcomplete.specification.CountrySpecificationBuilder;
import hu.okrim.productreviewappcomplete.util.SqlExceptionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired
    CountryService countryService;
    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable String countryCode) {
        return new ResponseEntity<>(countryService.findByCountryCode(countryCode).getName(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> countries = countryService.findAll();
        if (countries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createCountry(@RequestBody CountryDTO countryDTO) {
        countryService.save(CountryMapper.mapToCountry(countryDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{countryCode}/delete")
    public ResponseEntity<?> deleteCountry(@PathVariable("countryCode") String countryCode){
        try {
            countryService.deleteByCountryCode(countryCode);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            String message = SqlExceptionMessageHandler.countryDeleteErrorMessage(ex);
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/multi-delete/{countryCodes}")
    public ResponseEntity<?> deleteCountries(@PathVariable("countryCodes") String[] countryCodes){
        try{
            for(String countryCode : countryCodes) {
                countryService.deleteByCountryCode(countryCode);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            String message = SqlExceptionMessageHandler.countryDeleteErrorMessage(ex);
            return  new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{countryCode}/modify")
    public ResponseEntity<HttpStatus> modifyCountry(@PathVariable("countryCode") String countryCode, @RequestBody CountryDTO countryDTO){
        Country existingCountry = countryService.findByCountryCode(countryDTO.getCountryCode());

        if (existingCountry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingCountry.setName(countryDTO.getName());
        countryService.save(existingCountry);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Country>> searchCountries(@RequestParam(value = "searchText", required = false) String searchText,
                                                    @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                    @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("orderByColumn") String orderByColumn,
                                                    @RequestParam("orderByDirection") String orderByDirection) {
        CountrySpecificationBuilder<Country> countryCountrySpecificationBuilder = new CountrySpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "name" -> countryCountrySpecificationBuilder.withName(searchText);
                case "countryCode" -> countryCountrySpecificationBuilder.withCountryCode(searchText);
                default -> {
                }
                // Handle unknown search columns
            }
        }
        else {
            if(quickFilterValues != null && !quickFilterValues.isEmpty()){
                // When searchColumn is not provided all fields are searched
                countryCountrySpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Country> specification = countryCountrySpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Country> countriesPage = countryService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(countriesPage, HttpStatus.OK);
    }

}
