package com.example.management.controller;

import com.example.management.model.*;
import com.example.management.repository.CaseRepository;
import com.example.management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CaseManagementControllerTest {

    @Spy
    @InjectMocks
    CaseManagementController caseManagementController;

    @Mock
    UserRepository userRepository;

    @Mock
    CaseRepository caseRepository;

    @Spy
    UserModelAssembler userModelAssembler;

    @Spy
    CaseModelAssembler caseModelAssembler;


    @Test
    void getOneCase() {
        Case _case = new Case("test","test", Severity.MEDIUM);
        doReturn(Optional.of(_case)).when(caseRepository).findById(0L);
        EntityModel<Case> responseEntity = caseManagementController.getOneCase(0L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getContent()).isEqualTo(_case);
    }

    @Test
    void getCaseByUUID() {
        Case _case = new Case("test","test", Severity.MEDIUM);
        UUID uuid = UUID.randomUUID();
        doReturn(Optional.of(_case)).when(caseRepository).findByUuid(uuid);
        EntityModel<Case> responseEntity = caseManagementController.getCaseByUUID(uuid.toString());
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getContent()).isEqualTo(_case);
    }

    @Test
    void getCaseUser() {
        User user = new User("Lokesh", "Gupta@wp.pl");
        Case _case = new Case("test","test", Severity.MEDIUM);
        _case.setUser(user);
        doReturn(Optional.of(_case)).when(caseRepository).findById(0L);
        EntityModel<User> responseEntity = caseManagementController.getCaseUser(0L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getContent()).isEqualTo(user);
    }

    @Test
    void createCase() {
        User user = new User("Lokesh", "Gupta@wp.pl");
        Case _case = new Case("test","test", Severity.MEDIUM);
        doReturn(user).when(userRepository).findByUsernameAndEmail(user.getUsername(), user.getEmail());
        when(caseRepository.save(Mockito.any(Case.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        ResponseEntity<?> responseEntity = caseManagementController.createCase(new RequestWrapper(user, _case));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateCase() {
        Case _case = new Case("test","test", Severity.MEDIUM);
        Case updatedCase = new Case("testUpdated", "test", Severity.HIGH);
        doReturn(Optional.of(_case)).when(caseRepository).findById(0L);
        when(caseRepository.save(Mockito.any(Case.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        ResponseEntity<?> responseEntity = caseManagementController.updateCase(0L, updatedCase);

        EntityModel<Case> entityModel = (EntityModel<Case>) responseEntity.getBody();

        assertThat(entityModel.getContent().getTitle()).isEqualTo("testUpdated");
        assertThat(entityModel.getContent().getDescription()).isEqualTo("test");
        assertThat(entityModel.getContent().getSeverity()).isEqualTo(Severity.HIGH);
    }

    @Test
    void removeClosedCase_CaseNotClosed() {
        Case _case = new Case("test","test", "MEDIUM", "NEW");
        doReturn(Optional.of(_case)).when(caseRepository).findById(0L);
        ResponseEntity<String> responseEntity = caseManagementController.removeClosedCase(0L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void removeClosedCase_CaseIsClosed() {
        Case _case = new Case("test","test", "MEDIUM", "DONE");
        doReturn(Optional.of(_case)).when(caseRepository).findById(0L);
        ResponseEntity<String> responseEntity = caseManagementController.removeClosedCase(0L);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void removeAllClosedCases_NotAllClosed() {
        Long numOfDeletions = 1L;

        doReturn(numOfDeletions).when(caseRepository).removeAllByStatus(Status.DONE);
        ResponseEntity<String> responseEntity = caseManagementController.removeAllClosedCases();
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Successfully removed 1 records");
    }
}