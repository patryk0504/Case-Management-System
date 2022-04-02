package com.example.management.controller;
//

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import com.example.management.controller.UserManagementController;
import com.example.management.model.User;
import com.example.management.model.UserModelAssembler;
import com.example.management.repository.CaseRepository;
import com.example.management.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@WebMvcTest(UserManagementController.class)
public class UserManagementControllerTest {

    @Autowired private MockMvc mvc;

    @MockBean private UserRepository repository;
    @MockBean private CaseRepository caseRepository;
    @MockBean private UserModelAssembler userModelAssembler;

    @Test
    public void getAllShouldFetchAHalFormsEmbeddedDocument() throws Exception {

        given(repository.findAll()).willReturn(Arrays.asList( //
                new User("Frodo", "Baggins"), //
                new User("Bilbo", "Baggins")));

        mvc.perform(get("/api/users").accept(MediaTypes.HAL_FORMS_JSON_VALUE)) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON_VALUE))

                .andExpect(jsonPath("$._embedded.employees[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.employees[0].firstName", is("Frodo")))
                .andExpect(jsonPath("$._embedded.employees[0].lastName", is("Baggins")))
                .andExpect(jsonPath("$._embedded.employees[0].role", is("ring bearer")))
                .andExpect(jsonPath("$._embedded.employees[0]._templates.default.method", is("put")))
                .andExpect(jsonPath("$._embedded.employees[0]._templates.default.properties[0].name", is("firstName")))
                .andExpect(jsonPath("$._embedded.employees[0]._templates.default.properties[1].name", is("id")))
                .andExpect(jsonPath("$._embedded.employees[0]._templates.default.properties[2].name", is("lastName")))
                .andExpect(jsonPath("$._embedded.employees[0]._templates.default.properties[3].name", is("role")))
                .andExpect(jsonPath("$._embedded.employees[0]._links.self.href", is("http://localhost/employees/1")))
                .andExpect(jsonPath("$._embedded.employees[0]._links.employees.href", is("http://localhost/employees")))

                .andExpect(jsonPath("$._embedded.employees[1].id", is(2)))
                .andExpect(jsonPath("$._embedded.employees[1].firstName", is("Bilbo")))
                .andExpect(jsonPath("$._embedded.employees[1].lastName", is("Baggins")))
                .andExpect(jsonPath("$._embedded.employees[1].role", is("burglar")))
                .andExpect(jsonPath("$._embedded.employees[1]._templates.default.method", is("put")))
                .andExpect(jsonPath("$._embedded.employees[1]._templates.default.properties[0].name", is("firstName")))
                .andExpect(jsonPath("$._embedded.employees[1]._templates.default.properties[1].name", is("id")))
                .andExpect(jsonPath("$._embedded.employees[1]._templates.default.properties[2].name", is("lastName")))
                .andExpect(jsonPath("$._embedded.employees[1]._templates.default.properties[3].name", is("role")))
                .andExpect(jsonPath("$._embedded.employees[1]._links.self.href", is("http://localhost/employees/2")))
                .andExpect(jsonPath("$._embedded.employees[1]._links.employees.href", is("http://localhost/employees")))

                .andExpect(jsonPath("$._templates.default.method", is("post")))
                .andExpect(jsonPath("$._templates.default.properties[0].name", is("firstName")))
                .andExpect(jsonPath("$._templates.default.properties[1].name", is("id")))
                .andExpect(jsonPath("$._templates.default.properties[2].name", is("lastName")))
                .andExpect(jsonPath("$._templates.default.properties[3].name", is("role")))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees")));
    }

//    @Test
//    public void getOneShouldFetchASingleHalFormsDocument() throws Exception {
//
//        given(repository.findById(any())).willReturn(Optional.of(new Employee(1L, "Frodo", "Baggins", "ring bearer")));
//
//        mvc.perform(get("/employees/1").accept(MediaTypes.HAL_FORMS_JSON_VALUE)) //
//                .andDo(print()) //
//                .andExpect(status().isOk()) //
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON_VALUE))
//
//                .andExpect(jsonPath("$.id", is(1))) //
//                .andExpect(jsonPath("$.firstName", is("Frodo"))) //
//                .andExpect(jsonPath("$.lastName", is("Baggins"))) //
//                .andExpect(jsonPath("$.role", is("ring bearer")))
//
//                .andExpect(jsonPath("$._templates.default.method", is("put")))
//                .andExpect(jsonPath("$._templates.default.properties[0].name", is("firstName")))
//                .andExpect(jsonPath("$._templates.default.properties[1].name", is("id")))
//                .andExpect(jsonPath("$._templates.default.properties[2].name", is("lastName")))
//                .andExpect(jsonPath("$._templates.default.properties[3].name", is("role")))
//
//                .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees/1")))
//                .andExpect(jsonPath("$._links.employees.href", is("http://localhost/employees")));
//    }
}

//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
//
//import com.example.management.HypermediaConfiguration;
//import com.example.management.ManagementApplication;
//import com.example.management.model.Case;
//import com.example.management.model.Severity;
//import com.example.management.model.User;
//import com.example.management.model.UserModelAssembler;
//import com.example.management.repository.CaseRepository;
//import com.example.management.repository.UserRepository;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.MediaTypes;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.willReturn;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(UserManagementController.class)
//@AutoConfigureMockMvc
//@TestPropertySource(
//        locations = "classpath:application-test.properties")
//@ActiveProfiles("test")
//@Import({ HypermediaConfiguration.class })
//class UserManagementControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    CaseRepository caseRepository;
//
//    @MockBean
//    UserModelAssembler userModelAssembler;
//
//    @Test
//    void getOneUser() throws Exception {
//        String testUsername = "testUsername";
//        String testEmail = "testEmail@gmail.com";
//        User user = new User(testUsername, testEmail);
//        System.out.println("IDDDDDD: " + user.getId());
//        List<User> expected = new ArrayList<>();
//        expected.add(user);
//
//        when(userRepository.findAll()).thenReturn(expected);
//        System.out.println(userRepository.findAll().get(0).getId());
//
//        mvc.perform(MockMvcRequestBuilders.get("/api/users/" + user.getId()))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    void getAllUsers() throws Exception {
//        String testUsername = "testUsername";
//        String testEmail = "testEmail@gmail.com";
//        User user = new User(testUsername,testEmail);
//        User user2 = new User(testUsername + "2", testEmail + "2");
//        List<User> expected = new ArrayList<>();
//        expected.add(user);
//        expected.add(user2);
//
//        when(userRepository.findAll()).thenReturn(expected);
//
//        mvc.perform(MockMvcRequestBuilders.get("/api/users").accept(MediaTypes.HAL_FORMS_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}