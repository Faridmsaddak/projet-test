package org.machinestalk.api.mapper;

import org.machinestalk.api.dto.AddressDto;
import org.machinestalk.domain.Address;

public class AdressMapper {
    public static Address toEntity(AddressDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreetNumber(dto.getStreetNumber());
        address.setStreetName(dto.getStreetName());
        address.setPostalCode(dto.getPostalCode());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        return address;
    }

    public static AddressDto toDto(Address entity) {
        if (entity == null) return null;
        AddressDto dto = new AddressDto();
        dto.setStreetNumber(entity.getStreetNumber());
        dto.setStreetName(entity.getStreetName());
        dto.setPostalCode(entity.getPostalCode());
        dto.setCity(entity.getCity());
        dto.setCountry(entity.getCountry());
        return dto;
    }
}
