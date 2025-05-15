package org.machinestalk.service.impl;
import org.machinestalk.api.dto.AddressDto;
import org.machinestalk.api.dto.UserRegistrationDto;
import org.machinestalk.api.mapper.AdressMapper;
import org.machinestalk.api.mapper.UserMapper;
import org.machinestalk.domain.Address;
import org.machinestalk.domain.Department;
import org.machinestalk.domain.User;
import org.machinestalk.repository.UserRepository;
import org.machinestalk.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public User registerUser(final UserRegistrationDto userRegistrationDto) {
    try {
      if (userRegistrationDto == null) {
        throw new IllegalArgumentException("userRegistrationDto ne doit pas être null");
      }

      Department department = new Department();
      department.setName(userRegistrationDto.getDepartment());

      AddressDto addressDtoPrincipal = userRegistrationDto.getPrincipalAddress();
      AddressDto addressDtoSecondary = userRegistrationDto.getSecondaryAddress();

      Address addressPrincipal = AdressMapper.toEntity(addressDtoPrincipal);
      Address addressSecondary = AdressMapper.toEntity(addressDtoSecondary);

      Set<Address> addressList = new HashSet<>();
      addressList.add(addressPrincipal);
      addressList.add(addressSecondary);

      User user = new User();
      user.setFirstName(userRegistrationDto.getFirstName());
      user.setLastName(userRegistrationDto.getLastName());
      user.setDepartment(department);
      user.setAddresses(addressList);

      return userRepository.save(user);

    } catch (Exception e) {
      // Log ou gestion de l'erreur
      throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur", e);
    }
  }

  @Override
  public Mono<User> getById(final long id) {
    Optional<User> userOpt = userRepository.findById(id);
    return userOpt.map(Mono::just).orElseGet(Mono::empty);
  }
}