package org.machinestalk.repository.Controller;
import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.machinestalk.api.dto.UserDto;
import org.machinestalk.api.dto.UserRegistrationDto;
import org.machinestalk.api.impl.UserApiImpl;
import org.machinestalk.api.mapper.AdressMapper;
import org.machinestalk.domain.Address;
import org.machinestalk.domain.Department;
import org.machinestalk.domain.User;
import org.machinestalk.service.UserService;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
@WebFluxTest(UserApiImpl.class)
public class UserApiImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ModelMapper modelMapper;
    @Test
    void shouldRegisterUser() {
        Department department = new Department();
        department.setName("IT");

        Address address = new Address();
        address.setStreetNumber("20");
        address.setStreetName("Rue Habib Borgiba");
        address.setPostalCode("75015");
        address.setCity("Tunis");
        address.setCountry("Tunis");


        User user = new User();

        user.setId(1L);
        user.setFirstName("Msaddak");
        user.setLastName("Farid");
        user.setDepartment(department);
        user.setAddresses(Collections.singleton(address));

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setDepartment(department.getName());
        userRegistrationDto.setFirstName(user.getFirstName());
        userRegistrationDto.setLastName(user.getLastName());
        userRegistrationDto.setPrincipalAddress(AdressMapper.toDto(address));
        userRegistrationDto.setSecondaryAddress(AdressMapper.toDto(address));
        UserDto.UserInfos userInfos = new UserDto.UserInfos();
        userInfos.setFirstName("Msaddak");
        userInfos.setLastName("Farid");
        userInfos.setDepartment("IT");
        userInfos.setAdresses(Arrays.asList("20 Rue Habib Borgiba, 75015 Tunis, Tunis"));
        UserDto userDto = new UserDto("1", userInfos);
        Mockito.when(modelMapper.map(eq(user), eq(UserDto.class))).thenReturn(userDto);
        Mockito.when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(user);
        webTestClient
                .post()
                .uri("/api/users/register")
                .bodyValue(userRegistrationDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                    assert dto.getId().equals("1");
                    assert dto.getUserInfos() != null;
                    assert dto.getUserInfos().getFirstName().equals("Msaddak");
                    assert dto.getUserInfos().getLastName().equals("Farid");
                    assert dto.getUserInfos().getDepartment().equals("IT");
                    assert dto.getUserInfos().getAdresses().size() == 1;
                });
    }

    @Test
    void shouldGetUserById() {
        long userId = 1L;

        Department department = new Department();
        department.setName("IT");

        Address address = new Address();
        address.setStreetNumber("20");
        address.setStreetName("Rue Habib Borgiba");
        address.setPostalCode("75015");
        address.setCity("Tunis");
        address.setCountry("Tunis");

        User user = new User();
        user.setId(userId);
        user.setFirstName("Msaddak");
        user.setLastName("Farid");
        user.setDepartment(department);
        user.setAddresses(Collections.singleton(address));

        Mockito.when(userService.getById(userId)).thenReturn(Mono.just(user));

        webTestClient
                .get()
                .uri("/api/users/{id}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(u -> {
                    assert u != null;
                    assert u.getId().equals(String.valueOf(userId));

                    assert u.getUserInfos() != null;
                    assert u.getUserInfos().getFirstName().equals("Msaddak");
                });
    }
}
