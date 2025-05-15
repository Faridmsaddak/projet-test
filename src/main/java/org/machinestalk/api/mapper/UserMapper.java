package org.machinestalk.api.mapper;

import org.machinestalk.api.dto.AddressDto;
import org.machinestalk.api.dto.UserRegistrationDto;
import org.machinestalk.domain.Address;
import org.machinestalk.domain.Department;
import org.machinestalk.domain.User;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {

    public static User toEntity(UserRegistrationDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        Department department = new Department();
        department.setName(dto.getDepartment());
        user.setDepartment(department);

        Set<Address> addresses = new HashSet<>();
        if (dto.getPrincipalAddress() != null) {
            addresses.add(toAddressEntity(dto.getPrincipalAddress()));
        }
        if (dto.getSecondaryAddress() != null) {
            addresses.add(toAddressEntity(dto.getSecondaryAddress()));
        }

        user.setAddresses(addresses);

        return user;
    }

    private static Address toAddressEntity(AddressDto dto) {
        Address address = new Address();
        address.setStreetNumber(dto.getStreetNumber());
        address.setStreetName(dto.getStreetName());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        return address;
    }
}
