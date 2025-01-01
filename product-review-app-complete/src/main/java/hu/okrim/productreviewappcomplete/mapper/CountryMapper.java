package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.CountryDTO;
import hu.okrim.productreviewappcomplete.model.Country;

public class CountryMapper {
    public static CountryDTO mapToCountryDTO (Country country){
        return new CountryDTO(
                country.getCountryCode(),
                country.getName()
        );
    }
    public static Country mapToCountry (CountryDTO countryDTO){
        return new Country(
                countryDTO.getCountryCode(),
                countryDTO.getName()
        );
    }
}
