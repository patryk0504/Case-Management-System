package com.example.management.controller;


import com.example.management.model.User;
import com.example.management.model.UserModelAssembler;
import com.example.management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.platform.runner.JUnitPlatform;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserManagementControllerTest {

    @Spy
    @InjectMocks
    UserManagementController userManagementController;

    @Mock
    UserRepository userRepository;

    @Spy
    UserModelAssembler userModelAssembler;

    @Test
    public void getOneUserTest(){
        User user = new User("Lokesh", "Gupta@wp.pl");
        doReturn(Optional.of(user)).when(userRepository).findById(0L);
        EntityModel<User> responseEntity = userManagementController.getOneUser(0L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getContent()).isEqualTo(user);
    }

    @Test
    public void getAllUsersTest(){
        User user = new User("Lokesh", "Gupta@wp.pl");
        User user2 = new User("Lokesh2", "Gupta2@wp.pl");
        List<User> users = Arrays.asList(user, user2);

        doReturn(users).when(userRepository).findAll();
        CollectionModel<EntityModel<User>> responseEntity = userManagementController.getAllUsers();
        assertThat(responseEntity.getContent().size()).isEqualTo(2);
    }

}